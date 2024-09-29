package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.AuctionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionDto {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private BigDecimal startPrice;
    private AuctionStatus status;
    private BigDecimal minimumBidIncrement;
    private int extensionCount;
}
