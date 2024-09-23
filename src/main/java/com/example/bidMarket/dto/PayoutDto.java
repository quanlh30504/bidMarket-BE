package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PayoutDto {
    private UUID id;
    private UUID auctionId;  // Foreign Key đến bảng Auction
    private UUID sellerId;  // Foreign Key đến bảng User
    private UUID buyerId;  // Foreign Key đến bảng User
    private BigDecimal amount;
    private String payoutStatus;  // Trạng thái (PENDING, COMPLETED, FAILED)
    private LocalDateTime payoutInitiatedAt;
    private LocalDateTime payoutCompletedAt;
    private Boolean confirmationRequired;
    private LocalDateTime confirmationReceivedAt;
    private LocalDateTime autoPayoutDue;
}
