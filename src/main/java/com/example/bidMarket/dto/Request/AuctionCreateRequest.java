package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.ProductDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionCreateRequest {
    private String title;
    private ProductDto productDto;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private BigDecimal startingPrice;
    private AuctionStatus auctionStatus;
    private BigDecimal minimumBidIncrement;
    private int extensionCount;
}
