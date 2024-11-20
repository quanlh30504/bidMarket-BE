package com.example.bidMarket.controller;

import com.example.bidMarket.Enum.BidStatus;
//import com.example.bidMarket.KafkaService.BidProducer;
import com.example.bidMarket.MQTemplate.BidProviderMQ;
import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.service.BidService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {
    private final BidService bidService;
//    private final BidProducer bidProducer;
    private final BidProviderMQ bidProviderMQ;

    @PostMapping
    public ResponseEntity<?> placeBid(@RequestBody BidCreateRequest bidCreateRequest) {
//        bidProducer.sendBidRequest(bidCreateRequest);
        bidProviderMQ.sendBidRequest(bidCreateRequest);
        return ResponseEntity.ok("Send bid to kafka sucessfully");
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
