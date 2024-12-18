package com.example.bidMarket.dto.Response;

import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.Request.ProductCreateRequest;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuctionSearchResponse {
    private UUID id;
    private String title;
    private ProductDto productDto;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private BigDecimal startingPrice;
    private BigDecimal minimumBidIncrement;
    private long bidCount;
    private int extensionCount;
}
