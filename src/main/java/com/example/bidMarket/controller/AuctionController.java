package com.example.bidMarket.controller;

import ch.qos.logback.core.sift.AppenderFactoryUsingSiftModel;
import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.dto.Response.ApiResponse;
import com.example.bidMarket.dto.Response.AuctionSearchResponse;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.service.AuctionService;
import com.example.bidMarket.service.BidService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final BidService bidService;
    private final AuctionMapper auctionMapper;

    @PostMapping()
    public ResponseEntity<AuctionDto> createAuction (@RequestBody AuctionCreateRequest request) throws Exception {
        return ResponseEntity.ok(auctionService.createAuction(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuctionDto> updateAuction(
            @PathVariable UUID id,
            @RequestPart AuctionUpdateRequest request,
            @RequestPart(value = "newImages", required = false) MultipartFile newImages) throws Exception {
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

    @PutMapping("/close/{auctionId}")
    public ResponseEntity<String> closeAuction(@PathVariable UUID auctionId) {
        auctionService.closeAuction(auctionId);
        return ResponseEntity.ok("Close successfully auction " + auctionId);
    }

    @PutMapping("/cancel/{auctionId}")
    public ResponseEntity<String> cancelAuction(@PathVariable UUID auctionId){
        auctionService.cancelAuction(auctionId);
        return ResponseEntity.ok("Cancel successfully auction " + auctionId);
    }

    @PutMapping("/open/{auctionId}")
    public ResponseEntity<String> openAuction(@PathVariable UUID auctionId) {
        auctionService.openAuction(auctionId);
        return ResponseEntity.ok("Open successfully auction " + auctionId);
    }

    @PutMapping("reOpen/{id}")
    public ResponseEntity<String> reOpenAuction(@PathVariable UUID id, @RequestBody AuctionUpdateRequest auctionUpdateRequest) {
        auctionService.reOpenAuction(id, auctionUpdateRequest);
        return ResponseEntity.ok("Reopen successfully auction " + id);
    }

    @GetMapping("/{auctionId}/bids")
    public PaginatedResponse<BidDto> getBidsHistoryOfAuction(
            @PathVariable UUID auctionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "VALID") BidStatus status,
            @RequestParam(defaultValue = "bidTime") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        Page<BidDto> bidDtos = bidService.getBidsOfAuction(auctionId, page, size,status, sortField, direction);
        return new PaginatedResponse<>(
                bidDtos.getNumber(),
                bidDtos.getSize(),
                bidDtos.getTotalElements(),
                bidDtos.getTotalPages(),
                bidDtos.isLast(),
                bidDtos.isFirst(),
                bidDtos.stream().toList()
        );

    }
}

