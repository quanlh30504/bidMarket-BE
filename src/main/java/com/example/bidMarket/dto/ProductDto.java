package com.example.bidMarket.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private String name;
    private String description;  // Mô tả sản phẩm
    private Integer categoryId;  // Foreign Key đến bảng Category
    private UUID sellerId;  // Foreign Key đến bảng User
    private String status;  // Trạng thái (ACTIVE, INACTIVE, SOLD, REMOVED)
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
