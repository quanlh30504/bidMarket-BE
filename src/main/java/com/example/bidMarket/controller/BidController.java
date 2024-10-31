package com.example.bidMarket.controller;

import com.example.bidMarket.KafkaService.BidProducer;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Request.AutoPlaceBidRequest;
import com.example.bidMarket.model.Bid;
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

//    @PostMapping("/placeBid")
//    public ResponseEntity<?> placeBid(@RequestBody Object bidRequest) {
//        if (bidRequest instanceof AutoPlaceBidRequest autoPlaceBidRequest) {
//            if (autoPlaceBidRequest.isAuto()) {
//                bidService.autoPlaceBid(autoPlaceBidRequest);
//            }
//        } else if (bidRequest instanceof BidCreateRequest createRequest) {
//            bidService.createBid(createRequest);
//        }
//        return ResponseEntity.ok("Bid placed successfully");
//    }
    @PostMapping("/placeBid")
    public ResponseEntity<String> placeBid(@RequestBody BidCreateRequest bidCreateRequest) {
        if (bidCreateRequest.isAuto()) {
            bidService.autoPlaceBid(bidCreateRequest); // Gọi service đặt giá tự động
        } else if (!bidCreateRequest.isAuto()) {
            bidService.createBid(bidCreateRequest); // Gọi service đặt giá thủ công
        } else {
            throw new IllegalArgumentException("Request invalid!!!");
        }
        return ResponseEntity.ok("Bid placed successfully");
    }
}



