package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID auctionId;
    private UUID userId;
    private BigDecimal paymentAmount;
    private PaymentStatus status;
    private LocalDateTime paymentDue;
    private LocalDateTime paymentMadeAt;
}
