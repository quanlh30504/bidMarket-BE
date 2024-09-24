package com.example.bidMarket.service;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.mapper.BidMapper;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidMapper bidMapper;

    public BidDto createBid(BidDto bidDto) {
        Bid bid = bidMapper.bidDtoToBid(bidDto);

        bid.setStatus(Bid.Status.VALID);
        bid = bidRepository.save(bid);

        return bidMapper.bidToBidDto(bid);
    }

    public BidDto getBidById(UUID id) {
        return bidRepository.findById(id)
                .map(bidMapper::bidToBidDto)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
    }

    public List<BidDto> getBidsByAuction(UUID auctionId) {
        return bidRepository.findByAuctionId(auctionId).stream()
                .map(bidMapper::bidToBidDto)
                .collect(Collectors.toList());
    }

    public BidDto updateBid(UUID id, BidDto bidDto) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
        // Cập nhật bid từ DTO
        bidMapper.updateBidFromDto(bidDto, bid);
        bid = bidRepository.save(bid);
        return bidMapper.bidToBidDto(bid);
    }

    public void deleteBid(UUID id) {
        bidRepository.deleteById(id);
    }

    public List<BidDto> getAllBids() {
        return bidRepository.findAll().stream()
                .map(bidMapper::bidToBidDto)
                .collect(Collectors.toList());
    }
}
