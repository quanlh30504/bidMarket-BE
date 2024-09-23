package com.example.bidMarket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer categoryId;  // Foreign Key to Category (replace later)

    @Column(nullable = false)
    private UUID sellerId;  // Foreign Key to User (seller)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'ACTIVE'")
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int stockQuantity = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Enum for product status
    public enum ProductStatus {
        ACTIVE,
        INACTIVE,
        SOLD,
        REMOVED
    }
}

