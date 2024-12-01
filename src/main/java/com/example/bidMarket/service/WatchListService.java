package com.example.bidMarket.service;

import com.example.bidMarket.model.WatchList;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface WatchListService {
    public Page<WatchList> getWatchlistByUserId(UUID userId, int page, int size, String sortBy, String sortDirection);
    WatchList addToWatchlist(UUID userId, UUID auctionId) throws Exception;
    void removeFromWatchlist(UUID id) throws Exception;


    WatchList getWatchlistByUserIdAndAuctionId(UUID userId, UUID auctionId);
}
