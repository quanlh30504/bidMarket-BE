package com.example.bidMarket.controller;

import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping
    public ResponseEntity<AuctionDto> createAuction(@RequestBody AuctionDto auctionDto) {
        AuctionDto createdAuction = auctionService.createAuction(auctionDto);
        return ResponseEntity.ok(createdAuction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDto> getAuctionById(@PathVariable UUID id) {
        AuctionDto auctionDto = auctionService.getAuctionById(id);
        return ResponseEntity.ok(auctionDto);
    }

    @GetMapping
    public ResponseEntity<List<AuctionDto>> getAllAuctions() {
        List<AuctionDto> auctions = auctionService.getAllAuctions();
        return ResponseEntity.ok(auctions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionDto> updateAuction(@PathVariable UUID id, @RequestBody AuctionDto auctionDto) {
        AuctionDto updatedAuction = auctionService.updateAuction(id, auctionDto);
        return ResponseEntity.ok(updatedAuction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable UUID id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }
}
