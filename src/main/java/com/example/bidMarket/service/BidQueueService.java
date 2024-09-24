package com.example.bidMarket.service;

import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.mapper.BidQueueMapper;
import com.example.bidMarket.model.BidQueue;
import com.example.bidMarket.repository.BidQueueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BidQueueService {

    private final BidQueueRepository bidQueueRepository;
    private final BidQueueMapper bidQueueMapper;

    public BidQueueService(BidQueueRepository bidQueueRepository, BidQueueMapper bidQueueMapper) {
        this.bidQueueRepository = bidQueueRepository;
        this.bidQueueMapper = bidQueueMapper;
    }

    public BidQueueDto createBidQueue(BidQueueDto bidQueueDto) {
        BidQueue bidQueue = bidQueueMapper.toEntity(bidQueueDto);
        bidQueue = bidQueueRepository.save(bidQueue);
        return bidQueueMapper.toDto(bidQueue);
    }

    public BidQueueDto getBidQueueById(UUID id) {
        return bidQueueRepository.findById(id)
                .map(bidQueueMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Bid Queue not found"));
    }

    public List<BidQueueDto> getBidQueueByAuction(UUID auctionId) {
        return bidQueueRepository.findByAuctionId(auctionId).stream()
                .map(bidQueueMapper::toDto)
                .collect(Collectors.toList());
    }

    public BidQueueDto updateBidQueue(UUID id, BidQueueDto bidQueueDto) {
        BidQueue bidQueue = bidQueueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bid Queue not found"));
        bidQueueMapper.updateEntity(bidQueueDto, bidQueue);
        bidQueue = bidQueueRepository.save(bidQueue);
        return bidQueueMapper.toDto(bidQueue);
    }

    public void deleteBidQueue(UUID id) {
        bidQueueRepository.deleteById(id);
    }
}
