package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.AddressType;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressDto {
    private UUID id;
    private UUID userId;
    private AddressType addressType;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
