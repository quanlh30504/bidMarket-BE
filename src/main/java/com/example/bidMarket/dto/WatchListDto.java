package com.example.bidMarket.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchListDto {
    private UUID id;
    private UUID userId;
    private UUID auctionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
