package com.example.bidMarket.dto;

import lombok.Data;

@Data
public class ProductImageDto {
    private String imageUrl;
    private Boolean isPrimary;

    public boolean isPrimary() {
        return isPrimary;
    }
}
