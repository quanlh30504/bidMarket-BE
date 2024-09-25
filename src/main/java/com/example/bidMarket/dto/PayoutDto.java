package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PayoutDto {
    private UUID id;
    private UUID auctionId;
    private UUID sellerId;
    private UUID buyerId;
    private BigDecimal amount;
    private String payoutStatus;  // Trạng thái (PENDING, COMPLETED, FAILED)
    private Timestamp payoutInitiatedAt;
    private Timestamp payoutCompletedAt;
    private Boolean confirmationRequired;
    private Timestamp confirmationReceivedAt;
    private Timestamp autoPayoutDue;
}
