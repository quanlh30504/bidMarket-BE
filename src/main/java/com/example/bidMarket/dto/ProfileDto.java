package com.example.bidMarket.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfileDto {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl;
}
