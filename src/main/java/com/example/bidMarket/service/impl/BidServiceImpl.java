package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Request.AutoPlaceBidRequest;
import com.example.bidMarket.dto.Response.BidCreateResponse;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.BidMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.BidRepository;
import com.example.bidMarket.repository.PaymentRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.BidService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final PaymentRepository paymentRepository;

    private final BidMapper bidMapper;

    @Override
    @Transactional
    public BidCreateResponse createBid(BidCreateRequest bidCreateRequest) {
        User user = userRepository.findById(bidCreateRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(bidCreateRequest.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        Bid bid = Bid.builder()
                .userId(bidCreateRequest.getUserId())
                .auction(auction)
                .bidAmount(bidCreateRequest.getBidAmount())
                .status(BidStatus.PENDING)
                .build();
        return BidCreateResponse.builder()
                .id(bidRepository.save(bid).getId())
                .build();
    }


    @Override
    @Transactional
    public void processBid(BidCreateRequest bidRequest) {
        Optional<Auction> auctionOpt = auctionRepository.findById(bidRequest.getAuctionId());
        if (auctionOpt.isEmpty()) {
            throw new IllegalArgumentException("Auction not found");
        }

        Auction auction = auctionOpt.get();

        // Lưu lệnh đặt giá vào DB
        Bid bid = Bid.builder()
                .auction(auction)
                .userId(bidRequest.getUserId())
                .bidAmount(bidRequest.getBidAmount())
                .status(BidStatus.VALID)
                .bidTime(bidRequest.getBidTime())
                .build();

        // Kiểm tra thời gian đặt lệnh
        if (bidRequest.getBidTime().isBefore(auction.getStartTime()) || bidRequest.getBidTime().isAfter(auction.getEndTime())
            || bidRequest.getBidTime().isBefore(auction.getLastBidTime())
        ) {
            log.error("Bid time is invalid");
            bid.setStatus(BidStatus.INVALID);
        }

        // Kiểm tra số tiền đặt giá
        if (bidRequest.getBidAmount().compareTo(auction.getCurrentPrice().add(auction.getMinimumBidIncrement())) < 0) {
            log.error("Bid amount is too low");
            bid.setStatus(BidStatus.INVALID);
        }

        if (bid.getStatus() == BidStatus.VALID) {
            // Cập nhật phiên đấu giá
            auction.setCurrentPrice(bidRequest.getBidAmount());
            auction.setLastBidTime(bidRequest.getBidTime());
            auctionRepository.save(auction);
        }
        bidRepository.save(bid);
    }

    // Service này lấy tat ca cac bid cua auction (Valid và Invalid)
    @Override
    public Page<BidDto> getAllBidsOfAuction(UUID auctionId, int page, int size, String sortField, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Bid> bids = bidRepository.findAllByAuctionId(auctionId, pageable);

        if (bids.isEmpty()) {
            log.error("No bids found for auction ID: " + auctionId);
            throw new AppException(ErrorCode.BID_NOT_FOUND);
        }

        return bids.map(bidMapper::bidToBidDto);
    }

    // Service lấy các bid hop le (VALID) của auction
    @Override
    public Page<BidDto> getBidsOfAuction(UUID auctionId, int page, int size, BidStatus status, String sortField, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Bid> bids = bidRepository.findAllByAuctionIdAndStatus(auctionId, BidStatus.VALID, pageable);

        if (bids.isEmpty()) {
            log.warn("No valid bids found for auction ID: " + auctionId);
            throw new AppException(ErrorCode.BID_NOT_FOUND);
        }

        return bids.map(bidMapper::bidToBidDto);

    }

    @Override
    @Transactional
    public void autoPlaceBid(AutoPlaceBidRequest autoPlaceBidRequest) {
        Auction auction = auctionRepository.findById(autoPlaceBidRequest.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        // Nếu maxBid null hoặc người dùng không chọn chế độ tự động hoặc maxBid <= BidAmount (lần đầu)
        if (autoPlaceBidRequest.getMaxBid() == null || !autoPlaceBidRequest.isAutoBid()
                || autoPlaceBidRequest.getMaxBid().compareTo(autoPlaceBidRequest.getBidAmount()) <= 0) {

            // Khi người dùng muốn đặt giá thủ công lượt này
            if (autoPlaceBidRequest.getMaxBid() != null &&
                    autoPlaceBidRequest.getMaxBid().compareTo(autoPlaceBidRequest.getBidAmount()) > 0) {
                autoPlaceBidRequest.setAutoBid(true);
                // Check nếu ra giá < giá cao nhất -> lỗi
                Bid highestBid = bidRepository.findHighestBidByAuctionId(autoPlaceBidRequest.getAuctionId());

                if (highestBid != null && autoPlaceBidRequest.getBidAmount().compareTo(highestBid.getBidAmount()) <= 0) {
                    throw new AppException(ErrorCode.BID_TOO_LOW);
                }
            }

            Bid newBid = Bid.builder()
                    .auction(auction)
                    .userId(autoPlaceBidRequest.getUserId())
                    .bidAmount(autoPlaceBidRequest.getBidAmount())
                    .maxBid(autoPlaceBidRequest.getMaxBid())
                    .build();
            bidRepository.save(newBid);


            return;
        }

        // Khi maxBid có, MaxBid phải > BidAmount
        if (autoPlaceBidRequest.getMaxBid().compareTo(autoPlaceBidRequest.getBidAmount()) <= 0) {
            throw new AppException(ErrorCode.MAX_BID_TOO_LOW);
        }

        if (autoPlaceBidRequest.isAutoBid()) {
            checkAndPlaceAutoBid(auction, autoPlaceBidRequest.getUserId(), autoPlaceBidRequest.getMaxBid());
        }
    }

    private void checkAndPlaceAutoBid(Auction auction, UUID userId, BigDecimal maxBid) {
        Bid highestBid = bidRepository.findHighestBidByAuctionId(auction.getId());

        if (highestBid != null && highestBid.getUserId().equals(userId)) {
            return;
        }

        BigDecimal newBidAmount = highestBid.getBidAmount().add(new BigDecimal("10.00")); // Tăng thêm 10
        if (newBidAmount.compareTo(maxBid) <= 0) {
            Bid autoBid = Bid.builder()
                    .auction(auction)
                    .userId(userId)
                    .bidAmount(newBidAmount)
                    .maxBid(maxBid)
                    .build();
            bidRepository.save(autoBid);
        }
    }
}
