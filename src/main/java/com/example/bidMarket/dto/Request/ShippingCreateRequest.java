package com.example.bidMarket.dto.Request;

import com.example.bidMarket.model.Bid;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShippingCreateRequest {
    private UUID auctionId;
    private UUID sellerId;
    private UUID buyerId;
    private BigDecimal price;
}
