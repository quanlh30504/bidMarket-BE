package com.example.bidMarket.controller;

import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bid-queues")
public class BidQueueController {
    private final BidService bidService;

    public BidQueueController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<BidQueueDto> addBidToQueue(@RequestBody BidQueueDto bidQueueDto) {
        BidQueueDto createdBidQueue = bidService.addBidToQueue(bidQueueDto);
        return ResponseEntity.ok(createdBidQueue);
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidQueueDto>> getBidQueueByAuction(@PathVariable UUID auctionId) {
        List<BidQueueDto> bidQueue = bidService.getBidQueueByAuction(auctionId);
        return ResponseEntity.ok(bidQueue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BidQueueDto> updateBidQueue(@PathVariable UUID id, @RequestBody BidQueueDto bidQueueDto) {
        BidQueueDto updatedBidQueue = bidService.updateBidQueue(id, bidQueueDto);
        return ResponseEntity.ok(updatedBidQueue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBidQueue(@PathVariable UUID id) {
        bidService.deleteBidQueue(id);
        return ResponseEntity.ok().build();
    }
}
