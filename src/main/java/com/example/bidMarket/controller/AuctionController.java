package com.example.bidMarket.controller;

import com.example.bidMarket.dto.AuctionCreateDto;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping()
    public ResponseEntity<Auction> createAuction (@RequestBody AuctionCreateDto request) {
        Auction auction = auctionService.createAuction(request);
        return ResponseEntity.ok(auction);
    }
}

