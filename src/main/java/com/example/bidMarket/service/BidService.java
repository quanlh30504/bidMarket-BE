package com.example.bidMarket.service;

import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Response.BidCreateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface BidService {

    BidCreateResponse createBid(BidCreateRequest bidCreateRequest) throws Exception;

    void processBid (BidCreateRequest bidCreateRequest);

    Page<BidDto> getAllBidsOfAuction(UUID auctionId, int page, int size, String sortField, Sort.Direction direction);

    Page<BidDto> getBidsOfAuction(UUID auctionId, int page, int size, BidStatus status, String sortField, Sort.Direction direction);
}
