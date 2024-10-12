package com.example.bidMarket.service;

import com.example.bidMarket.model.WatchList;

import java.util.List;
import java.util.UUID;

public interface WatchListService {
    List<WatchList> getWatchlistByUserId(UUID userId) throws Exception;

    List<WatchList> getWatchlistByAuctionId(UUID auctionId) throws Exception;
    WatchList addToWatchlist(UUID userId, UUID auctionId) throws Exception;
    void removeFromWatchlist(UUID id) throws Exception;
}
