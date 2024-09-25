package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Ánh xạ từ Product entity sang ProductDto
//    @Mapping(target = "productId", source = "product.id")
    ProductDto productToProductDto(Product product);

    // Ánh xạ từ ProductDto sang Product entity
    Product productDtoToProduct(ProductDto productDto);

    // Cập nhật thông tin từ ProductDto vào Product entity
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);
}
