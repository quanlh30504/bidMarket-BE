package com.example.bidMarket.service;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;

import java.util.UUID;

public interface AuctionService {
    public AuctionDto createAuction (AuctionCreateRequest auctionCreateRequest);
    public AuctionDto changeAuctionStatus (UUID id, AuctionStatus status) throws Exception;
}
