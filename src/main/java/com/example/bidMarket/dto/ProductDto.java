package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private ProductStatus productStatus;
    private UUID sellerId;
    private int stockQuantity;
    private List<ProductImageDto> productImages;
    private List<CategoryType> categories;
}
