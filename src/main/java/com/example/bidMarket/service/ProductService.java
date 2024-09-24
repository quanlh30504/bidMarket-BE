package com.example.bidMarket.service;

import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);
        product = productRepository.save(product);
        return productMapper.productToProductDto(product);
    }

    public ProductDto getProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto updateProduct(UUID id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.updateProductFromDto(productDto, product);
        product = productRepository.save(product);
        return productMapper.productToProductDto(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
