package com.example.bidMarket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class ProfileDto {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl;
}
