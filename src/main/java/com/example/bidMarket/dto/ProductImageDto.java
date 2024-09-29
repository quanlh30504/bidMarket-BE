package com.example.bidMarket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageDto {
    private String imageUrl;
    private Boolean isPrimary;

    public boolean isPrimary() {
        return isPrimary;
    }
}
