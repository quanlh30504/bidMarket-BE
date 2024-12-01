package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.BidStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BidDto {
    private UUID userId;
    private UUID auctionId;
    private String userEmail;
    private BigDecimal bidAmount;
    private BidStatus status;
    private LocalDateTime bidTime;
}
