package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductImageDto;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class ProductUpdateRequest {
    private String name;

    private String description;

    private int stockQuantity;

    private List<ProductImageDto> productImages;

    private Set<CategoryType> categories;
}
