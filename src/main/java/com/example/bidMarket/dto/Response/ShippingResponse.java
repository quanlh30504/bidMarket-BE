package com.example.bidMarket.dto.Response;

import com.example.bidMarket.Enum.ShippingStatus;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingResponse {
    private UUID id;
    private UUID auctionId;
    private UUID sellerId;
    private UUID buyerId;
    private String productName;
    private String productImageUrl;
    private String shopName;
    private int quantity;
    private BigDecimal price;
    private ShippingStatus status;

}
