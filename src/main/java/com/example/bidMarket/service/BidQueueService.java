package com.example.bidMarket.service;

import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.BidQueue;
import com.example.bidMarket.repository.BidQueueRepository; // Giả sử bạn đã tạo repo cho BidQueue
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BidQueueService {
    @Autowired
    private BidQueueRepository bidQueueRepository;

    public BidQueueDto addBidToQueue(BidQueueDto bidQueueDto) {
        BidQueue bidQueue = new BidQueue();

        bidQueue.setAuctionId(bidQueueDto.getAuctionId());
        bidQueue.setUserId(bidQueueDto.getUserId());
        bidQueue.setBidAmount(bidQueueDto.getBidAmount());

        bidQueue = bidQueueRepository.save(bidQueue);
        return new BidQueueDto(bidQueue);
    }

    public List<BidQueueDto> getBidQueueByAuction(UUID auctionId) {
        return bidQueueRepository.findByAuctionId(auctionId).stream()
                .map(BidQueueDto::new)
                .collect(Collectors.toList());
    }

    public BidQueueDto updateBidQueue(UUID id, BidQueueDto bidQueueDto) {
        Bid bid = bidQueueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bid not found in queue"));
        bid.setBidAmount(bidQueueDto.getBidAmount());
        // Cập nhật bid trong hàng đợi
        bid = bidQueueRepository.save(bid);
        return new BidQueueDto(bid);
    }

    public void deleteBidQueue(UUID id) {
        bidQueueRepository.deleteById(id);
    }
}
