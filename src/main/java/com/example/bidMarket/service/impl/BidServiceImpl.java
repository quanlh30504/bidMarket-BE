package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Response.BidCreateResponse;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.Payment;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.BidRepository;
import com.example.bidMarket.repository.PaymentRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.BidService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Service;

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

    @Override
    @Transactional
    public BidCreateResponse createBid(BidCreateRequest bidCreateRequest) throws Exception {
        User user = userRepository.findById(bidCreateRequest.getUserId())
                .orElseThrow(() -> new Exception("Not existed user id " + bidCreateRequest.getUserId()));
        Auction auction = auctionRepository.findById(bidCreateRequest.getAuctionId())
                .orElseThrow(() -> new Exception("Not exited auction id " + bidCreateRequest.getAuctionId()));
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


}
