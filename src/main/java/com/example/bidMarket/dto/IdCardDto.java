package com.example.bidMarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.UUID;


@Data
public class IdCardDto {
    private UUID id;
    private UUID userId;
    private String idCard;
    private String frontImageUrl;
    private String backImageUrl;
    private LocalDate issuedDate;
    private LocalDate expirationDate;

}

