package com.example.bidMarket.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WatchListDto {
    private UUID id;
    private UUID userId;
    private UUID auctionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
