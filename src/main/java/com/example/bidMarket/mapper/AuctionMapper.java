package com.example.bidMarket.mapper;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.AuctionCreateDto;
import com.example.bidMarket.dto.ProductImageDto;
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
    public Auction auctionCreateToAuction(AuctionCreateDto auctionCreateDto, Product product) {
        Auction auction = new Auction();
        auction.setTitle(auctionCreateDto.getTitle());
        auction.setStartTime(auctionCreateDto.getStartTime());
        auction.setEndTime(auctionCreateDto.getEndTime());
        auction.setCurrentPrice(auctionCreateDto.getStartPrice());
        auction.setStartingPrice(auctionCreateDto.getStartPrice());
        auction.setStatus(AuctionStatus.PENDING);
        auction.setMinimumBidIncrement(auctionCreateDto.getMinimumBidIncrement());
        auction.setExtensionCount(0);
        auction.setProduct(product);
        return auction;
    }

    public User auctionCreateToUser(AuctionCreateDto auctionCreateDto) {
        UUID sellerId = auctionCreateDto.getProductDto().getSeller();
        return userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
    }

    public Product auctionCreateToProduct(AuctionCreateDto auctionCreateDto) {
        Product product = new Product();
        product.setName(auctionCreateDto.getProductDto().getName());
        product.setDescription(auctionCreateDto.getProductDto().getDescription());
        product.setSeller(auctionCreateToUser(auctionCreateDto));
        product.setStatus(ProductStatus.INACTIVE);
        product.setStockQuantity(auctionCreateDto.getProductDto().getStockQuantity());

        if (auctionCreateDto.getProductDto().getProductImages() != null && !auctionCreateDto.getProductDto().getProductImages().isEmpty()) {
            List<ProductImage> productImages = auctionCreateDto.getProductDto().getProductImages().stream()
                    .map(image -> productMapper.productImageDtoToProductImage(image))
                    .collect(Collectors.toList());
            product.setProductImages(productImages);
        }

        return product;
    }




}
