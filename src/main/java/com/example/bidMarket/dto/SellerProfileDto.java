package com.example.bidMarket.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SellerProfileDto {
    private UUID id;
    private UUID userId;
    private String fullName;
    private String phoneNumber;
    private String idCard;
    private String profileImageUrl;
    private Double averageRating;
}
