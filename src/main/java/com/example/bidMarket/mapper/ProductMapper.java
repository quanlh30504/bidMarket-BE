package com.example.bidMarket.mapper;


import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.ProductImageDto;
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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductImage productImageDtoToProductImage(ProductImageDto productImageDto) {
        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(productImageDto.getImageUrl());
        productImage.setPrimary(productImageDto.isPrimary());

        return productImage;
    }
    public ProductImageDto productImageToProductImageDto(ProductImage productImage) {
        return ProductImageDto.builder()
                .imageUrl(productImage.getImageUrl())
                .isPrimary(productImage.isPrimary())
                .build();
    }

    public Product productDtoToProduct(ProductDto productDto) throws Exception {
        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new Exception("Don't exist user id: " + productDto.getSellerId()));
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSeller(seller);
        product.setStatus(productDto.getProductStatus());
        product.setStockQuantity(productDto.getStockQuantity());

        List<ProductImageDto> productImageDtoList = productDto.getProductImages();
        if (productImageDtoList!= null && !productImageDtoList.isEmpty()) {
            List<ProductImage> productImages = productImageDtoList.stream()
                    .map(this::productImageDtoToProductImage)
                    .collect(Collectors.toList());
            product.setProductImages(productImages);
        }
        List<CategoryType> categoryTypeList = productDto.getCategories();
        if (categoryTypeList != null && !categoryTypeList.isEmpty()) {
            List<Category> categories = categoryTypeList.stream()
                    .map(categoryType -> categoryRepository.findByCategoryType(categoryType)
                            .orElseThrow(() -> new RuntimeException("Category not found: " + categoryType))
                    )
                    .toList();
            product.setCategories(categories);
        }
        return product;
    }

    public ProductDto productToProductDto (Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(productDto.getName());
        productDto.setDescription(productDto.getDescription());
        productDto.setProductStatus(product.getStatus());
        productDto.setSellerId(product.getSeller().getId());
        productDto.setStockQuantity(productDto.getStockQuantity());

        List<ProductImage> productImages = product.getProductImages();
        if (productImages == null && !productImages.isEmpty()) {
            List<ProductImageDto> productImageDtoList = productImages.stream()
                    .map(this::productImageToProductImageDto)
                    .collect(Collectors.toList());
            productDto.setProductImages(productImageDtoList);
        }
        List<Category> categories = product.getCategories();
        if (categories != null && !categories.isEmpty()) {
            List<CategoryType> categoryTypeList = categories.stream()
                    .map(Category::getCategoryType)
                    .toList();
            productDto.setCategories(categoryTypeList);
        }
        return productDto;
    }

}
