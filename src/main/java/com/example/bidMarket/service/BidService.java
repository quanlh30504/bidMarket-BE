package com.example.bidMarket.service;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.BidQueue;
import com.example.bidMarket.repository.BidQueueRepository;
import com.example.bidMarket.repository.BidRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;
    private BidQueueRepository bidQueueRepository;
    public BidDto createBid(BidDto bidDto) {
        Bid bid = new Bid();
        bid.setAuctionId(bidDto.getAuctionId());
        bid.setUserId(bidDto.getUserId());
        bid.setBidAmount(bidDto.getBidAmount());
        bid = bidRepository.save(bid);
        return new BidDto(bid);
    }

    public BidDto getBidById(UUID id) {
        return bidRepository.findById(id)
                .map(BidDto::new)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
    }

    public List<BidDto> getBidsByAuction(UUID auctionId) {
        return bidRepository.findById(auctionId).stream()
                .map(BidDto::new)
                .collect(Collectors.toList());
    }

    public BidQueueDto addBidToQueue(BidQueueDto bidQueueDto) {
        // Logic to add bid to queue
        Bid bid = new Bid();
        bid.setAuctionId(bidQueueDto.getAuctionId());
        bid.setUserId(bidQueueDto.getUserId());
        bid.setBidAmount(bidQueueDto.getBidAmount());
        // Save to queue (this is a placeholder, replace with actual queue logic)
        bid = bidRepository.save(bid);
        return new BidQueueDto(bid);
    }

    public List<BidQueueDto> getBidQueueByAuction(UUID auctionId) {
        // Logic to get bid queue by auctionId
        return bidRepository.findById(auctionId).stream()
                .map(BidQueueDto::new)
                .collect(Collectors.toList());
    }

    public BidQueue updateBidQueue(UUID id, BidQueueDto bidQueueDto) {
        // Logic to update bid in queue
        BidQueue bidQueue = bidQueueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BidQueue not found"));
        bidQueue.setBidAmount(bidQueueDto.getBidAmount());

        bidQueue = bidQueueRepository.save(bidQueue);
        return new BidQueueDto(bidQueue);
    }

    public void deleteBidQueue(UUID id) {
        // Logic to delete bid from queue
        bidQueueRepository.deleteById(id);
    }
}