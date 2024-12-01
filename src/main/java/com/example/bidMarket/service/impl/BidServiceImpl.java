package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.BidEvent;
import com.example.bidMarket.dto.CommentEvent;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Response.BidCreateResponse;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.BidMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.Payment;
import com.example.bidMarket.model.User;
import com.example.bidMarket.notification.CreateNotificationRequest;
import com.example.bidMarket.notification.NotificationService;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.BidRepository;
import com.example.bidMarket.repository.PaymentRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.BidService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private final SimpMessagingTemplate simpMessagingTemplate;


    private final NotificationService notificationService;

    @Override
    @Transactional
    public BidCreateResponse createBid(BidCreateRequest bidCreateRequest) throws Exception {
        User user = userRepository.findById(bidCreateRequest.getUserId())
                .orElseThrow(() -> new Exception("Not existed user id " + bidCreateRequest.getUserId()));
        Auction auction = auctionRepository.findById(bidCreateRequest.getAuctionId())
                .orElseThrow(() -> new Exception("Not exited auction id " + bidCreateRequest.getAuctionId()));
        Bid bid = Bid.builder()
                .user(user)
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
        User user = userRepository.findById(bidRequest.getUserId())
                        .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("Start process bid");
        Optional<Auction> auctionOpt = auctionRepository.findById(bidRequest.getAuctionId());
        if (auctionOpt.isEmpty()) {
            throw new IllegalArgumentException("Auction not found");
        }

        Auction auction = auctionOpt.get();

        // Lưu lệnh đặt giá vào DB
        Bid bid = Bid.builder()
                .auction(auction)
                .user(user)
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
        Bid saveBid = bidRepository.save(bid);

        simpMessagingTemplate.convertAndSend("/topic/bids/auctionId/" + saveBid.getAuction().getId(),
                new BidEvent("create",bidMapper.bidToBidDto(saveBid)));

//        notificationService.createNotification(CreateNotificationRequest.builder()
//                        .userId(bidRequest.getUserId())
//                        .message(String.format("Your bid in auction %s successfully", bidRequest.getAuctionId()))
//                        .build());


    }

    // Service này lấy tat ca cac bid cua auction (Valid và Invalid)
    // Test
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
//            throw new AppException(ErrorCode.BID_NOT_FOUND);
        }
        return bids.map(bidMapper::bidToBidDto);
    }

    @Override
    public long getBidCountOfAuction(UUID auctionId) {
        return bidRepository.countByAuctionIdAndStatus(auctionId, BidStatus.VALID);
    }
}
