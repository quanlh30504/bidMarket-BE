package com.example.bidMarket.dto.Response;

import com.example.bidMarket.Enum.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderResponse {
    UUID id;
    UUID auctionId;
    UUID userId;
    String userEmail;
    String auctionTitle;
    String productImageUrl;
    BigDecimal totalAmount;
    OrderStatus status;
    LocalDateTime paymentDueDate;
    LocalDateTime createAt;
}
