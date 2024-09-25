package com.example.bidMarket.service;

import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.repository.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionMapper auctionMapper;

    public AuctionService(AuctionRepository auctionRepository, AuctionMapper auctionMapper) {
        this.auctionRepository = auctionRepository;
        this.auctionMapper = auctionMapper;
    }

    public AuctionDto createAuction(AuctionDto auctionDto) {
        Auction auction = auctionMapper.auctionDtoToAuction(auctionDto);
        auction = auctionRepository.save(auction);
        return auctionMapper.auctionToAuctionDto(auction);
    }

    public AuctionDto getAuctionById(UUID id) {
        return auctionRepository.findById(id)
                .map(auctionMapper::auctionToAuctionDto)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    public List<AuctionDto> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(auctionMapper::auctionToAuctionDto)
                .collect(Collectors.toList());
    }

    public AuctionDto updateAuction(UUID id, AuctionDto auctionDto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        auctionMapper.updateAuctionFromDto(auctionDto, auction);
        auction = auctionRepository.save(auction);
        return auctionMapper.auctionToAuctionDto(auction);
    }

    public void deleteAuction(UUID id) {
        auctionRepository.deleteById(id);
    }
}
