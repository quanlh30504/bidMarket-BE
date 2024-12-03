package com.example.bidMarket.service;

import com.example.bidMarket.dto.AddressDto;

import java.util.UUID;

public interface AddressService {
    AddressDto updateOrCreateAddress(UUID userId, AddressDto addressDto);
    AddressDto createAddress(AddressDto addressDto);

}
