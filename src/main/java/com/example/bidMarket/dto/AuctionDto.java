package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuctionDto {
    private UUID id;
    private String title;
    private UUID productId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private BigDecimal startingPrice;
    private String status;  // Trạng thái (PENDING, OPEN, CLOSED, v.v.)
    private BigDecimal minimumBidIncrement;
    private int extensionCount;  // Số lần gia hạn
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}