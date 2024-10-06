package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
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
import com.example.bidMarket.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final AuctionMapper auctionMapper;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public AuctionDto createAuction(AuctionCreateRequest auctionCreateRequest) throws Exception {
        // Tạo Product entity từ DTO
        Product product = productService.createProduct(auctionCreateRequest.getProductCreateRequest());

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

    @Override
    @Transactional
    public Auction updateAuction(UUID id, AuctionUpdateRequest request) {
        log.info("Start update auction");
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() != AuctionStatus.PENDING && auction.getStatus() != AuctionStatus.CANCELED){
            throw new AppException(ErrorCode.AUCTION_UPDATE_FAILED);
        }
        if (request.getStartTime().isAfter(request.getEndTime())){
            throw new AppException(ErrorCode.AUCTION_UPDATE_FAILED);
        }
        auction.setTitle(request.getTitle());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setCurrentPrice(request.getStartingPrice());
        auction.setMinimumBidIncrement(request.getMinimumBidIncrement());

        productService.updateProduct(auction.getProduct().getId(), request.getProductUpdateRequest());

        auction = auctionRepository.save(auction);
        return auction;
    }

    @Override
    @Transactional
    public void deleteAuction(UUID id) {
        log.info("Start delete auction");
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (    auction.getStatus() == AuctionStatus.OPEN ||
                auction.getStatus() == AuctionStatus.EXTENDED ||
                auction.getStatus() == AuctionStatus.CLOSED) {
            throw new AppException(ErrorCode.AUCTION_DELETE_FAILED);
        }
        Product product = auction.getProduct();
        if (auction.getStatus() == AuctionStatus.PENDING) {
            product.setStatus(ProductStatus.INACTIVE);
            productRepository.save(product);
        }else if (auction.getStatus() == AuctionStatus.CANCELED || auction.getStatus() == AuctionStatus.COMPLETED) {
            productRepository.delete(product);
        }
        auctionRepository.delete(auction);
    }

    @Override
    public void openAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.PENDING){
            if (auction.getStartTime().isBefore(LocalDateTime.now())){
                throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
            }
            auction.setStatus(AuctionStatus.OPEN);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.ACTIVE);
            auctionRepository.save(auction);
            productRepository.save(product);
        }else {
            throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
        }
    }

    @Override
    public void closeAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.OPEN){
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
        }else {
            throw new AppException(ErrorCode.AUCTION_CLOSE_FAILED);
        }
    }

    @Override
    public void cancelAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() != AuctionStatus.COMPLETED){
            auction.setStatus(AuctionStatus.CANCELED);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.INACTIVE);
            auctionRepository.save(auction);
            productRepository.save(product);
        }else {
            throw new AppException(ErrorCode.AUCTION_CANCEL_FAILED);
        }
    }

    @Override
    public void completeAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.CLOSED){
            auction.setStatus(AuctionStatus.COMPLETED);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.SOLD);
            auctionRepository.save(auction);
            productRepository.save(product);
        }else {
            throw new AppException(ErrorCode.AUCTION_COMPLETE_FAILED);
        }
    }

    @Override
    public void reOpenAuction(UUID id, AuctionUpdateRequest request) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.CANCELED){
            auction = updateAuction(id, request);
            auction.setStatus(AuctionStatus.PENDING);
            auctionRepository.save(auction);
        }else {
            throw new AppException(ErrorCode.AUCTION_COMPLETE_FAILED);
        }
    }


}
