package com.example.bidMarket.dto.Request;


import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionUpdateRequest {
    private String title;
    private ProductUpdateRequest productUpdateRequest;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentPrice;
    private BigDecimal startingPrice;
    private BigDecimal minimumBidIncrement;
    private int extensionCount;
}
