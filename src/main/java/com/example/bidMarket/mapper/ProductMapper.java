package com.example.bidMarket.mapper;


import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.dto.Request.ProductCreateRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Category;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.CategoryRepository;
import com.example.bidMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductImage productImageDtoToProductImage(ProductImageDto productImageDto) {
        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(productImageDto.getImageUrl());
        productImage.setPrimary(productImageDto.getIsPrimary());

        return productImage;
    }
    public ProductImageDto productImageToProductImageDto(ProductImage productImage) {
        return ProductImageDto.builder()
                .imageUrl(productImage.getImageUrl())
                .isPrimary(productImage.isPrimary())
                .build();
    }

    public Product productDtoToProduct(ProductDto productDto) {
        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSeller(seller);
        product.setStatus(productDto.getProductStatus() == null ? ProductStatus.INACTIVE : productDto.getProductStatus());
        product.setStockQuantity(productDto.getStockQuantity());

        Set<CategoryType> categoryTypeList = productDto.getCategories();
        if (categoryTypeList != null && !categoryTypeList.isEmpty()) {
            Set<Category> categories = categoryTypeList.stream()
                    .map(categoryType -> categoryRepository.findByCategoryType(categoryType)
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED))
                    )
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }
        return product;
    }
    public Product productCreateToProduct(ProductCreateRequest request) {
        User seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setSeller(seller);
        product.setStatus(ProductStatus.INACTIVE);
        product.setStockQuantity(request.getStockQuantity());

        Set<CategoryType> categoryTypeList = request.getCategories();
        if (categoryTypeList != null && !categoryTypeList.isEmpty()) {
            Set<Category> categories = categoryTypeList.stream()
                    .map(categoryType -> categoryRepository.findByCategoryType(categoryType)
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED))
                    )
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }
        return product;
    }


    public ProductDto productToProductDto (Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setProductStatus(product.getStatus());
        productDto.setSellerId(product.getSeller().getId());
        productDto.setStockQuantity(product.getStockQuantity());

        List<ProductImage> productImages = product.getProductImages();
        if (productImages != null && !productImages.isEmpty()) {
            List<ProductImageDto> productImageDtoList = productImages.stream()
                    .map(this::productImageToProductImageDto)
                    .collect(Collectors.toList());
            productDto.setProductImages(productImageDtoList);
        }
        Set<Category> categories = product.getCategories();
        if (categories != null && !categories.isEmpty()) {
            Set<CategoryType> categoryTypeList = categories.stream()
                    .map(Category::getCategoryType)
                    .collect(Collectors.toSet());
            productDto.setCategories(categoryTypeList);
        }
        return productDto;
    }

}
