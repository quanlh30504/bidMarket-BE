package com.example.bidMarket.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
    private UUID followerId;
    private UUID sellerId;
    private LocalDateTime followedAt;
}
