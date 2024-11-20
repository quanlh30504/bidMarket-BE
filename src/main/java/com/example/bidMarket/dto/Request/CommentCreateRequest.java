package com.example.bidMarket.dto.Request;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentCreateRequest {
    private UUID userId;
    private UUID auctionId;
    private String content;
}