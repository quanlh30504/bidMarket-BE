package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductImageDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private UUID sellerId;
    private Integer stockQuantity;
    private Set<CategoryType> categories;
    private List<String> imageUrls;
}
