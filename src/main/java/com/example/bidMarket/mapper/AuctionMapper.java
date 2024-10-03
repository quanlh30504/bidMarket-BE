package com.example.bidMarket.mapper;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuctionMapper {
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    // Mapping AuctionCreateDto to Auction entity
    public Auction auctionCreateToAuction(AuctionCreateRequest auctionCreateRequest, Product product) {
        Auction auction = new Auction();
        auction.setTitle(auctionCreateRequest.getTitle());
        auction.setStartTime(auctionCreateRequest.getStartTime());
        auction.setEndTime(auctionCreateRequest.getEndTime());
        auction.setLastBidTime(auctionCreateRequest.getStartTime());
        auction.setCurrentPrice(auctionCreateRequest.getStartingPrice());
        auction.setStartingPrice(auctionCreateRequest.getStartingPrice());
        auction.setStatus(AuctionStatus.PENDING);
        auction.setMinimumBidIncrement(auctionCreateRequest.getMinimumBidIncrement());
        auction.setExtensionCount(0);
        auction.setProduct(product);
        return auction;
    }

    public User auctionCreateToUser(AuctionCreateRequest auctionCreateRequest) {
        UUID sellerId = auctionCreateRequest.getProductDto().getSellerId();
        return userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
    }

    public Product auctionCreateToProduct(AuctionCreateRequest auctionCreateRequest) throws Exception {
        return productMapper.productDtoToProduct(auctionCreateRequest.getProductDto());
    }

    public AuctionDto auctionToAuctionDto(Auction auction) {
        return AuctionDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .productId(auction.getProduct().getId())
                .sellerId(auction.getProduct().getSeller().getId())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .currentPrice(auction.getCurrentPrice())
                .lastBidTime(auction.getLastBidTime())
                .startingPrice(auction.getStartingPrice())
                .status(auction.getStatus())
                .minimumBidIncrement(auction.getMinimumBidIncrement())
                .extensionCount(auction.getExtensionCount())
                .build();
    }




}
