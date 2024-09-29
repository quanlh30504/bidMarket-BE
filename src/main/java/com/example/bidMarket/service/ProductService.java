package com.example.bidMarket.service;

import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    public ProductDto getProduct(UUID id) throws Exception;
    public List<ProductDto> getAllProduct();
    public ProductDto createProduct(ProductDto productDto) throws Exception;
    public void deleteProduct(UUID productId);
    public ProductDto updateProduct(UUID id, ProductDto productDto) throws Exception;
    public ProductDto updateStatus(UUID id, ProductStatus status) throws Exception;
}
