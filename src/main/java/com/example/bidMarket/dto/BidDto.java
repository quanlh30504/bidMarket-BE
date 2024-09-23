package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BidDto {
    private UUID id;
    private UUID auctionId;  // Foreign Key đến bảng Auction
    private UUID userId;  // Foreign Key đến bảng User
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private LocalDateTime updatedAt;
    private String status;  // Trạng thái (VALID, INVALID)
}
