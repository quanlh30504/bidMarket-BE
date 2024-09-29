package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.repository.ProductRepository;
import com.example.bidMarket.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto getProduct(UUID id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Not exist product id: " + id));
        return productMapper.productToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.productToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) throws Exception {
        Product product = productMapper.productDtoToProduct(productDto);
        productRepository.save(product);
        return productMapper.productToProductDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(UUID id, ProductDto productDto) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Not exist product id " + id));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStockQuantity(productDto.getStockQuantity());

        productRepository.save(product);
        return productMapper.productToProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateStatus(UUID id, ProductStatus status) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Not exist product id " + id));
        product.setStatus(status);
        productRepository.save(product);
        return productMapper.productToProductDto(product);
    }
}
