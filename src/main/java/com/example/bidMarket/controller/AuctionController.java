package com.example.bidMarket.controller;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.dto.Response.ApiResponse;
import com.example.bidMarket.dto.Response.AuctionSearchResponse;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.service.AuctionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    @PutMapping("/{id}")
    public ResponseEntity<AuctionDto> updateAuction(@PathVariable UUID id, @RequestBody AuctionUpdateRequest request){
        Auction auction = auctionService.updateAuction(id, request);
        return ResponseEntity.ok(auctionMapper.auctionToAuctionDto(auction));
    }

    @GetMapping
    public ResponseEntity<List<AuctionDto>> getAllAuction(){
        return ResponseEntity.ok(auctionService.getAllAuction());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDto> getAuctionById(@PathVariable UUID id){
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    @GetMapping("/search")
    public PaginatedResponse<AuctionSearchResponse> searchAuctions(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) CategoryType categoryType,
            @RequestParam(required = false) AuctionStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "currentPrice") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {

        log.info("Start search auction");
        Page<Auction> auctions = auctionService.searchAuctions(title, categoryType, status, minPrice, maxPrice, startTime, endTime, page, size, sortField, sortDirection);
//        return auctions.map(auctionMapper::auctionToAuctionDto);
        List<AuctionSearchResponse> content = auctions.getContent().stream().map(auctionMapper::auctionToAuctionSearchResponse).toList();

        return new PaginatedResponse<>(
                auctions.getNumber(),
                auctions.getSize(),
                auctions.getTotalElements(),
                auctions.getTotalPages(),
                auctions.isLast(),
                auctions.isFirst(),
                content
        );

    }


}

