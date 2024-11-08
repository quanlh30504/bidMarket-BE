package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
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
import java.time.LocalDateTime;
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
    public void autoPlaceBid(BidCreateRequest bidCreateRequest) {
        Auction auction = auctionRepository.findById(bidCreateRequest.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        Bid highestBid = bidRepository.findHighestBidByAuctionId(auction.getId());

        // Kiểm tra xem phiên đấu giá đã kết thúc chưa
        if (AuctionStatus.CLOSED.equals(auction.getStatus())) {
            throw new AppException(ErrorCode.AUCTION_ALREADY_CLOSED);
        }

        // Người đầu tiên đặt giá -> bắt buộc phải đặt giá thủ công
        if (highestBid == null) {
            throw new AppException(ErrorCode.FIRST_BID_MANUAL);
        }

        // Lấy maxBid từ request hoặc từ DB -> giải quyết được vấn đề lưu lại maxBid sau mỗi lần tự động đặt giá
        BigDecimal maxBid =  bidCreateRequest.getMaxBid();


        //  MaxBid < HighestBid -> báo lỗi để người dùng chọn lại Auto hay không
        if (highestBid.getBidAmount().compareTo(maxBid) >= 0) {
            throw new AppException(ErrorCode.MAX_BID_EXCEEDED);
        }

        // Giá tự động phải cao hơn giá hiện tại ít nhất là minimumBidIncrement
        if (auction.getMinimumBidIncrement().compareTo(bidCreateRequest.getIncreAmount()) > 0) {
            throw new AppException(ErrorCode.BID_INCREAMOUNT_TOO_LOW);
        }
        checkAndPlaceAutoBid(auction, bidCreateRequest.getUserId(), maxBid, bidCreateRequest.getIncreAmount(), bidCreateRequest.getBidTime());
    }

    private void checkAndPlaceAutoBid(Auction auction, UUID userId, BigDecimal maxBid, BigDecimal increAmount, LocalDateTime bidTime) {
        Bid highestBid = bidRepository.findHighestBidByAuctionId(auction.getId());

        if (highestBid != null && highestBid.getUserId().equals(userId)) {
            return;
        }

        BigDecimal newBidAmount = highestBid.getBidAmount().add(increAmount); // Tăng thêm increAmount

        // Nếu newBidAmount <= maxBid -> đặt giá mới
        if (newBidAmount.compareTo(maxBid) <= 0) {
            Bid autoBid = Bid.builder()
                    .auction(auction)
                    .userId(userId)
                    .bidAmount(newBidAmount)
                    .status(BidStatus.VALID)
                    .bidTime(bidTime)
                    .build();
            bidRepository.save(autoBid);
        } else {
            throw new AppException(ErrorCode.NEW_BID_TOO_HIGH);
        }
    }
}
