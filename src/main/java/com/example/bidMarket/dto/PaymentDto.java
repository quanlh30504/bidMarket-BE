package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID id;
    private UUID auctionId;
    private UUID buyerId;
    private BigDecimal paymentAmount;
    private String paymentStatus;  // Trạng thái (PENDING, COMPLETED, FAILED)
    private LocalDateTime paymentDue;
    private LocalDateTime paymentMadeAt;
    private BigDecimal systemFee;
}
