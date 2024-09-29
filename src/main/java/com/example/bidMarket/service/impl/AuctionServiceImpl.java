package com.example.bidMarket.service.impl;

import com.example.bidMarket.dto.AuctionCreateDto;
import com.example.bidMarket.dto.ProductImageDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public Auction createAuction(AuctionCreateDto auctionCreateDto) {
        // Tạo Product entity từ DTO
        Product product = auctionMapper.auctionCreateToProduct(auctionCreateDto);
        List<ProductImageDto> productImageDtoList = auctionCreateDto.getProductDto().getProductImages();
        // Lưu hình ảnh sản phẩm
        if (productImageDtoList != null && !productImageDtoList.isEmpty()) {
            List<ProductImage> productImageList= new ArrayList<>();
            for (ProductImageDto imageDto : productImageDtoList) {
                ProductImage productImage = productMapper.productImageDtoToProductImage(imageDto);
                productImage.setProduct(product);
                productImageList.add(productImage);
            }
            product.setProductImages(productImageList);
        }
        product = productRepository.save(product);

        // Tạo phiên đấu giá từ DTO và product đã được lưu
        Auction auction = auctionMapper.auctionCreateToAuction(auctionCreateDto, product);
        // Lưu phiên đấu giá vào database
        return auctionRepository.save(auction);
    }
}
