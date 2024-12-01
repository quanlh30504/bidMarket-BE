package com.example.bidMarket.service;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.Request.ProductCreateRequest;
import com.example.bidMarket.dto.Request.ProductUpdateRequest;
import com.example.bidMarket.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    public ProductDto getProduct(UUID id) throws Exception;
    public List<ProductDto> getAllProduct();
    Product createProduct(ProductCreateRequest request) throws Exception;
    public void deleteProduct(UUID productId);
    public ProductDto updateProduct(UUID id, ProductUpdateRequest request);
    public ProductDto updateStatus(UUID id, ProductStatus status) throws Exception;
    public Page<Product> searchProducts(UUID sellerId, String name, CategoryType categoryType, ProductStatus status,
                                        int page, int size, String sortField, Sort.Direction sortDirection);
}
