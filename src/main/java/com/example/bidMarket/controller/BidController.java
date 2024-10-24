package com.example.bidMarket.controller;

import com.example.bidMarket.KafkaService.BidProducer;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Request.AutoPlaceBidRequest;
import com.example.bidMarket.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {
    private final BidService bidService;
    private final BidProducer bidProducer;

    @PostMapping
    public ResponseEntity<?> createBid(@RequestBody BidCreateRequest bidCreateRequest) {
        bidProducer.sendBidRequest(bidCreateRequest);
        return ResponseEntity.ok("Send bid to kafka sucessfully");
    }

    @PostMapping("/placeBid")
    public ResponseEntity<String> autoPlaceBid(@RequestBody AutoPlaceBidRequest request) {
        bidService.autoPlaceBid(request);
        return ResponseEntity.ok("Bid placed successfully");
    }

//    @PostMapping("/placeBid")
//    public ResponseEntity<?> placeBid(@RequestBody AutoPlaceBidRequest autoPlaceBidRequest) {
//        if (autoPlaceBidRequest.isAutoBid() && autoPlaceBidRequest.getMaxBid() != null) {
//            bidService.autoPlaceBid(autoPlaceBidRequest);
//        } if (!autoPlaceBidRequest.isAutoBid() && autoPlaceBidRequest.getMaxBid() == null) {
//            BidCreateRequest bidCreateRequest = new BidCreateRequest();
//            bidCreateRequest.setAuctionId(autoPlaceBidRequest.getAuctionId());
//            bidCreateRequest.setUserId(autoPlaceBidRequest.getUserId());
//            bidCreateRequest.setBidAmount(autoPlaceBidRequest.getBidAmount());
//            bidService.createBid(bidCreateRequest);
//
//            if (autoPlaceBidRequest.getMaxBid() != null) {
//                autoPlaceBidRequest.setAutoBid(true);
//            }
//        }
//        return ResponseEntity.ok("Bid processed successfully");
//    }
}
