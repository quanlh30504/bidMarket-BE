package com.example.bidMarket.controller;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.dto.Response.ApiResponse;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;
    private final AuctionMapper auctionMapper;

    @PostMapping()
    public ResponseEntity<AuctionDto> createAuction (@RequestBody AuctionCreateRequest request) throws Exception {
        return ResponseEntity.ok(auctionService.createAuction(request));
    }
    @PutMapping("/{auctionId}/status")
    public ResponseEntity<AuctionDto> changeAuctionStatus(
            @PathVariable UUID auctionId,
            @RequestParam("newStatus") AuctionStatus newStatus) throws Exception {
        return ResponseEntity.ok(auctionService.changeAuctionStatus(auctionId, newStatus));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionDto> updateAuction(@PathVariable UUID id, @RequestBody AuctionUpdateRequest request){
        Auction auction = auctionService.updateAuction(id, request);
        return ResponseEntity.ok(auctionMapper.auctionToAuctionDto(auction));
    }
}

