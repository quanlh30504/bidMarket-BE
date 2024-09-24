package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.model.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    // Ánh xạ từ ProductImage entity sang ProductImageDto
    ProductImageDto productImageToProductImageDto(ProductImage productImage);

    // Ánh xạ từ ProductImageDto sang ProductImage entity
    ProductImage productImageDtoToProductImage(ProductImageDto productImageDto);

    // Cập nhật thông tin từ ProductImageDto vào ProductImage entity
    void updateProductImageFromDto(ProductImageDto productImageDto, @MappingTarget ProductImage productImage);
}
