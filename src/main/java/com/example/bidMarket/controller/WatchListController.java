package com.example.bidMarket.controller;

import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.Response.WatchListResponse;
import com.example.bidMarket.dto.WatchListDto;
import com.example.bidMarket.mapper.WatchListMapper;
import com.example.bidMarket.model.WatchList;
import com.example.bidMarket.service.impl.WatchListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public PaginatedResponse<WatchListResponse> getWatchListByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection

    ) {
        Page<WatchList> watchListPage = watchlistService.getWatchlistByUserId(userId, page, size, sortBy, sortDirection);
        List<WatchListResponse> content = watchListPage.getContent().stream()
                .map(WatchListMapper::watchlistToWatchlistResponse)
                .toList();
        return new PaginatedResponse<>(
                watchListPage.getNumber(),
                watchListPage.getSize(),
                watchListPage.getTotalElements(),
                watchListPage.getTotalPages(),
                watchListPage.isLast(),
                watchListPage.isFirst(),
                content
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWatchlist(@RequestParam String userId, @RequestParam String auctionId) throws Exception {
        WatchList watchlist = watchlistService.addToWatchlist(UUID.fromString(userId), UUID.fromString(auctionId));
        return ResponseEntity.ok(WatchListDto.builder()
                        .id(watchlist.getId())
                        .userId(watchlist.getUser().getId())
                        .auctionId(watchlist.getAuction().getId())
                        .build());
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable String id) {
        watchlistService.removeFromWatchlist(UUID.fromString(id));
        return ResponseEntity.ok("Watchlist item removed");
    }

    @GetMapping("/getWatchlist")
    public ResponseEntity<UUID> getWatchlistByUserAndAuction(@RequestParam String userId, @RequestParam String auctionId){
        WatchList watchList = watchlistService.getWatchlistByUserIdAndAuctionId(UUID.fromString(userId), UUID.fromString(auctionId));
        if (watchList == null){
            return ResponseEntity.ok(null);
        }else{
            return ResponseEntity.ok(watchList.getId());
        }
    }

}