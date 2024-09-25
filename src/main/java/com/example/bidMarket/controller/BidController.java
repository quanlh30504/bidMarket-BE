package com.example.bidMarket.controller;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<BidDto> createBid(@RequestBody BidDto bidDto) {
        BidDto createdBid = bidService.createBid(bidDto);
        return ResponseEntity.ok(createdBid);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidDto> getBidById(@PathVariable UUID id) {
        BidDto bid = bidService.getBidById(id);
        return ResponseEntity.ok(bid);
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidDto>> getBidByAuction(@PathVariable UUID auctionId) {
        List<BidDto> bidDtos = bidService.getBidsByAuction(auctionId);
        return ResponseEntity.ok(bidDtos);
    }
    @GetMapping
    public ResponseEntity<List<BidDto>> getAllBids() {
        List<BidDto> bids = bidService.getAllBids();
        return ResponseEntity.ok(bids);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BidDto> updateBid(@PathVariable UUID id, @RequestBody BidDto bidDto) {
        BidDto updatedBid = bidService.updateBid(id, bidDto);
        return ResponseEntity.ok(updatedBid);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable UUID id) {
        bidService.deleteBid(id);
        return ResponseEntity.noContent().build();
    }
}
