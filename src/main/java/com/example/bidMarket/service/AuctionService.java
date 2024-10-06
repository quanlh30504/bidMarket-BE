package com.example.bidMarket.service;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.model.Auction;

import java.util.UUID;

public interface AuctionService {
    public AuctionDto createAuction (AuctionCreateRequest auctionCreateRequest) throws Exception;
    public AuctionDto changeAuctionStatus (UUID id, AuctionStatus status) throws Exception;

    public Auction updateAuction(UUID id, AuctionUpdateRequest auctionUpdateRequest);
    public void deleteAuction(UUID id);
    public void openAuction(UUID id);
    public void closeAuction(UUID id);
    public void cancelAuction(UUID id);
    public void completeAuction(UUID id);
    public void reOpenAuction(UUID id, AuctionUpdateRequest request);

}
