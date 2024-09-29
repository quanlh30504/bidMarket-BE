package com.example.bidMarket.service;

import com.example.bidMarket.dto.AuctionCreateDto;
import com.example.bidMarket.model.Auction;

public interface AuctionService {
    public Auction createAuction (AuctionCreateDto auctionCreateDto);
}
