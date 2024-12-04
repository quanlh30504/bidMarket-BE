package com.example.bidMarket.dto.Request;

import lombok.*;

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
}
