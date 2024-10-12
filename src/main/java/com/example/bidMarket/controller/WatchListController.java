package com.example.bidMarket.controller;

import com.example.bidMarket.model.WatchList;
import com.example.bidMarket.service.impl.WatchListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListServiceImpl watchlistService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchList>> getUserWatchlist(@PathVariable UUID userId) throws Exception {
        List<WatchList> watchlist = watchlistService.getWatchlistByUserId(userId);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<WatchList>> getAuctionWatchlist(@PathVariable UUID auctionId) throws Exception {
        List<WatchList> watchlist = watchlistService.getWatchlistByAuctionId(auctionId);
        return ResponseEntity.ok(watchlist);
    }

    @PostMapping("/add")
    public ResponseEntity<WatchList> addToWatchlist(@RequestParam UUID userId, @RequestParam UUID auctionId) throws Exception {
        WatchList watchlist = watchlistService.addToWatchlist(userId, auctionId);
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable UUID id) {
        watchlistService.removeFromWatchlist(id);
        return ResponseEntity.ok("Watchlist item removed");
    }
}