package com.example.bidMarket.controller;

import com.example.bidMarket.KafkaService.BidProducer;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {
    private final BidService bidService;
    private final BidProducer bidProducer;

    @PostMapping
    public ResponseEntity<?> placeBid(@RequestBody BidCreateRequest bidCreateRequest) {
        try {
            bidProducer.sendBidRequest(bidCreateRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok("Successful");
    }

}
