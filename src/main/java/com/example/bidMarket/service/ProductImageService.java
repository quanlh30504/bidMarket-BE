package com.example.bidMarket.service;

import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.mapper.ProductImageMapper;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageMapper productImageMapper;

    public ProductImageDto createProductImage(ProductImageDto productImageDto) {
        ProductImage productImage = productImageMapper.productImageDtoToProductImage(productImageDto);
        productImage = productImageRepository.save(productImage);
        return productImageMapper.productImageToProductImageDto(productImage);
    }

    public ProductImageDto getProductImageById(UUID id) {
        return productImageRepository.findById(id)
                .map(productImageMapper::productImageToProductImageDto)
                .orElseThrow(() -> new RuntimeException("ProductImage not found"));
    }

    public List<ProductImageDto> getAllProductImages() {
        return productImageRepository.findAll().stream()
                .map(productImageMapper::productImageToProductImageDto)
                .collect(Collectors.toList());
    }

    public ProductImageDto updateProductImage(UUID id, ProductImageDto productImageDto) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductImage not found"));
        productImageMapper.updateProductImageFromDto(productImageDto, productImage);
        productImage = productImageRepository.save(productImage);
        return productImageMapper.productImageToProductImageDto(productImage);
    }

    public void deleteProductImage(UUID id) {
        productImageRepository.deleteById(id);
    }
}
