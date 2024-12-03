package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.ShippingStatus;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDto {
    private UUID id;
    private UUID auctionId;
    private UUID sellerId;
    private UUID buyerId;
    private ShippingStatus status;
    private String trackingNumber;
    private LocalDateTime shippedAt;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
}
