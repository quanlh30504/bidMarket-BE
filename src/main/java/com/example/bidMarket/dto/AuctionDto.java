package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuctionDto {
    private UUID id;
    private String title;
    private UUID productId;
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal currentPrice;
    private BigDecimal startingPrice;
    private String status;  // Trạng thái (PENDING, OPEN, CLOSED, v.v.)
    private BigDecimal minimumBidIncrement;
    private int extensionCount;  // Số lần gia hạn
    private Timestamp createdAt;
    private Timestamp updatedAt;
}