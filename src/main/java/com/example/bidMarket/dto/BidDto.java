package com.example.bidMarket.dto;

import com.example.bidMarket.model.Auction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BidDto {
    private UUID id;
    private UUID auctionId;
    private UUID userId;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private LocalDateTime updatedAt;
    private String status;  // Trạng thái (VALID, INVALID)
}
