package com.example.bidMarket.controller;

import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.service.BidQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bid-queue")
public class BidQueueController {

    private final BidQueueService bidQueueService;

    public BidQueueController(BidQueueService bidQueueService) {
        this.bidQueueService = bidQueueService;
    }

    @PostMapping
    public ResponseEntity<BidQueueDto> createBidQueue(@RequestBody BidQueueDto bidQueueDto) {
        BidQueueDto createdBidQueue = bidQueueService.createBidQueue(bidQueueDto);
        return ResponseEntity.ok(createdBidQueue);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidQueueDto> getBidQueueById(@PathVariable UUID id) {
        BidQueueDto bidQueueDto = bidQueueService.getBidQueueById(id);
        return ResponseEntity.ok(bidQueueDto);
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidQueueDto>> getBidQueueByAuction(@PathVariable UUID auctionId) {
        List<BidQueueDto> bidQueueDtos = bidQueueService.getBidQueueByAuction(auctionId);
        return ResponseEntity.ok(bidQueueDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BidQueueDto> updateBidQueue(@PathVariable UUID id, @RequestBody BidQueueDto bidQueueDto) {
        BidQueueDto updatedBidQueue = bidQueueService.updateBidQueue(id, bidQueueDto);
        return ResponseEntity.ok(updatedBidQueue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBidQueue(@PathVariable UUID id) {
        bidQueueService.deleteBidQueue(id);
        return ResponseEntity.ok().build();
    }
}
