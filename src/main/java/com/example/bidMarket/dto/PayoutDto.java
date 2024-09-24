package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
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
    private LocalDateTime payoutInitiatedAt;
    private LocalDateTime payoutCompletedAt;
    private Boolean confirmationRequired;
    private LocalDateTime confirmationReceivedAt;
    private LocalDateTime autoPayoutDue;
}
