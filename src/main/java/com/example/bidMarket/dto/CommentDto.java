package com.example.bidMarket.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Getter
@Setter
public class CommentDto {
    UUID id;
    UUID auctionId;
    UUID userId;
    String content;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
