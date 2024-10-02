package com.example.bidMarket.controller;

import com.example.bidMarket.KafkaService.KafkaProducer;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Response.BidCreateResponse;
import com.example.bidMarket.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {
    private final BidService bidService;
    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<?> placeBid(@RequestBody BidCreateRequest bidCreateRequest) {
        try {
            kafkaProducer.sendBidRequest(bidCreateRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok("Successful");
    }

}
