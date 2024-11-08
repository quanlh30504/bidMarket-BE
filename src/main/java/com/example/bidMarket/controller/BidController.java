package com.example.bidMarket.controller;

import com.example.bidMarket.KafkaService.BidProducer;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> placeBid(@RequestBody BidCreateRequest bidCreateRequest) {
        // Gửi cho kafka để xử lý
        bidProducer.sendBidRequest(bidCreateRequest);

        // Trả về response cho client để lượt tiếp theo biết được thông tin bid lần trước là auto hay không
        Map<String, Object> response = new HashMap<>();

        response.put("incrementAmount", bidCreateRequest.getIncreAmount());
        response.put("maxBid", bidCreateRequest.getMaxBid());
        response.put("autoBidStatus", bidCreateRequest.isAuto());
        response.put("message", "Bid placed successfully");

        return ResponseEntity.ok(response);
    }
}



