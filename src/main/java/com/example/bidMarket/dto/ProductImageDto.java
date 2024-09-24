package com.example.bidMarket.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductImageDto {
    private UUID id;
    private UUID productId;
    private String imageUrl;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}
