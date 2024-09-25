package com.example.bidMarket.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private String name;
    private String description;  // Mô tả sản phẩm
    private UUID sellerId;
    private String status;  // Trạng thái (ACTIVE, INACTIVE, SOLD, REMOVED)
    private Integer stockQuantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
