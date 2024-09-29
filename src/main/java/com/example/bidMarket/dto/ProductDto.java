package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.model.User;
import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private ProductStatus productStatus;
    private User seller;
    private int stockQuantity;
}
