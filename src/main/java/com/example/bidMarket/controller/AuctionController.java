package com.example.bidMarket.controller;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping()
    public ResponseEntity<AuctionDto> createAuction (@RequestBody AuctionCreateRequest request) {
        return ResponseEntity.ok(auctionService.createAuction(request));
    }
    @PutMapping("/{auctionId}/status")
    public ResponseEntity<AuctionDto> changeAuctionStatus(
            @PathVariable UUID auctionId,
            @RequestParam AuctionStatus newStatus) throws Exception {
        return ResponseEntity.ok(auctionService.changeAuctionStatus(auctionId, newStatus));
    }
}

