package com.example.bidMarket.service;

import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.repository.AuctionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    @Autowired
    private AuctionRepository auctionRepository;

    public AuctionDto createAuction(AuctionDto auctionDto) {
        Auction auction = new Auction();

        auction.setTitle(auctionDto.getTitle());
        auction.setProductId(auctionDto.getProductId());
        auction.setStartTime(auctionDto.getStartTime());
        auction.setEndTime(auctionDto.getEndTime());
        auction.setCurrentPrice(auctionDto.getCurrentPrice());
        auction.setStartingPrice(auctionDto.getStartingPrice());
        auction.setStatus(Auction.Status.valueOf(auctionDto.getStatus()));
        auction.setMinimumBidIncrement(auctionDto.getMinimumBidIncrement());
        auction = auctionRepository.save(auction);
        return convertToDto(auction);
    }

    public AuctionDto getAuctionById(UUID id) {
        return auctionRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    public List<AuctionDto> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AuctionDto updateAuction(UUID id, AuctionDto auctionDto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        auction.setTitle(auctionDto.getTitle());
        auction.setCurrentPrice(auctionDto.getCurrentPrice());
        auction.setStartingPrice(auctionDto.getStartingPrice());
        auction.setStatus(Auction.Status.valueOf(auctionDto.getStatus()));
        auction.setMinimumBidIncrement(auctionDto.getMinimumBidIncrement());
        auction.setEndTime(auctionDto.getEndTime());
        auction.setStartTime(auctionDto.getStartTime());
        auction.setExtensionCount(auctionDto.getExtensionCount());
        return convertToDto(auctionRepository.save(auction));
    }

    public void deleteAuction(UUID id) {
        auctionRepository.deleteById(id);
    }

    private AuctionDto convertToDto(Auction auction) {
        AuctionDto auctionDto = new AuctionDto();
        auctionDto.setId(auction.getId());
        auctionDto.setTitle(auction.getTitle());
        auctionDto.setProductId(auction.getProductId());
        auctionDto.setStartTime(auction.getStartTime());
        auctionDto.setEndTime(auction.getEndTime());
        auctionDto.setCurrentPrice(auction.getCurrentPrice());
        auctionDto.setStartingPrice(auction.getStartingPrice());
        auctionDto.setStatus(auction.getStatus().name());
        auctionDto.setMinimumBidIncrement(auction.getMinimumBidIncrement());
        auctionDto.setExtensionCount(auction.getExtensionCount());
        auctionDto.setCreatedAt(auction.getCreatedAt());
        auctionDto.setUpdatedAt(auction.getUpdatedAt());
        return auctionDto;
    }
}