package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class OrderDto {
    private UUID id;
    private UUID userId;
    private UUID auctionId;
    private BigDecimal totalAmount;
    private LocalDateTime paymentDueDate;
    private OrderStatus status;
}
