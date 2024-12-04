package com.example.bidMarket.dto.Response;

import com.example.bidMarket.Enum.AuctionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WatchListResponse {
    UUID id;
    UUID userId;
    UUID auctionId;
    String auctionTitle;
    String productImageUrl;
    LocalDateTime endTime;
    BigDecimal currentPrice;
    AuctionStatus status;
    BigDecimal bidAmount;
}
