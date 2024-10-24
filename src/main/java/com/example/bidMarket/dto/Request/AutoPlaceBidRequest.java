package com.example.bidMarket.dto.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AutoPlaceBidRequest {
    private UUID auctionId;
    private UUID userId;
    private BigDecimal bidAmount;
    private BigDecimal maxBid;
    private boolean autoBid = false;
}

