package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.ProductStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductDto {
    private String name;
    private String description;
    private ProductStatus productStatus;
    private UUID seller;
    private int stockQuantity;
    private List<ProductImageDto> productImages;
}
