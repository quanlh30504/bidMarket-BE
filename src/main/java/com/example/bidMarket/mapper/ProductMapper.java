package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.model.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductImage productImageDtoToProductImage(ProductImageDto productImageDto) {
        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(productImageDto.getImageUrl());
        productImage.setPrimary(productImageDto.isPrimary());

        return productImage;
    }
}
