package com.example.bidMarket.service.impl;

import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.User;
import com.example.bidMarket.model.WatchList;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.repository.WatchListRepository;
import com.example.bidMarket.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchListRepository watchlistRepository;
    private UserRepository userRepository;
    private AuctionRepository auctionRepository;

    @Override
    public Page<WatchList> getWatchlistByUserId(UUID userId, int page, int size, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return watchlistRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public WatchList addToWatchlist(UUID userId, UUID auctionId) {
        // Tìm kiếm đối tượng User và Auction
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        // Tạo một đối tượng Watchlist mới
        WatchList watchlist = new WatchList();
        watchlist.setUser(user);
        watchlist.setAuction(auction);

        return watchlistRepository.save(watchlist);
    }

    @Override
    public void removeFromWatchlist(UUID id) {
        // Logic to remove a watchlist item by id
        watchlistRepository.deleteById(id);
    }


}