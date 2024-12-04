package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.PaymentMethod;
import com.example.bidMarket.Enum.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentDto {
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private String transactionId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String bankCode;
    private LocalDateTime paymentDate;
    String productImageUrl;
}
