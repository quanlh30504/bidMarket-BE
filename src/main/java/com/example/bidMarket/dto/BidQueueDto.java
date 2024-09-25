package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BidQueueDto {
    private UUID id;
    private UUID auctionId;
    private UUID userId;
    private BigDecimal bidAmount;
    private Timestamp bidTime;
    private Timestamp updatedAt;
    private String status;  // Trạng thái (PENDING, PROCESSED, FAILED)
}
