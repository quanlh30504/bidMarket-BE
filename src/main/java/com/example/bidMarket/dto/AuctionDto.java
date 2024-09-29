package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.AuctionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuctionDto {
    private UUID id;
    private String title;
    private UUID productId;
    private UUID sellerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private BigDecimal startingPrice;
    private AuctionStatus status;
    private BigDecimal minimumBidIncrement;
    private int extensionCount;
}
