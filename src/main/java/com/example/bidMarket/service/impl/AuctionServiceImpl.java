package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.ProductImageRepository;
import com.example.bidMarket.repository.ProductRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.AuctionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final AuctionMapper auctionMapper;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuctionDto createAuction(AuctionCreateRequest auctionCreateRequest) throws Exception {
        // Tạo Product entity từ DTO
        Product product = productRepository.save(auctionMapper.auctionCreateToProduct(auctionCreateRequest));

        // Tạo phiên đấu giá từ DTO và product đã được lưu
        Auction auction = auctionMapper.auctionCreateToAuction(auctionCreateRequest, product);
        auction = auctionRepository.save(auction);

        AuctionDto auctionDto = auctionMapper.auctionToAuctionDto(auction);
        return auctionDto;
    }

    @Override
    @Transactional
    public AuctionDto changeAuctionStatus(UUID id, AuctionStatus status) throws Exception {
        Auction auction = auctionRepository.findById(id).orElseThrow(() -> new Exception("Not found auction id"));
        if (auction.getStatus() == AuctionStatus.COMPLETED || auction.getStatus() == AuctionStatus.CLOSED) {
            throw new Exception("Auction was completed or canceled, so can't change status");
        }
        auction.setStatus(status);
        return auctionMapper.auctionToAuctionDto(auctionRepository.save(auction));
    }




}
