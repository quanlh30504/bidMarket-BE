package com.example.bidMarket.service;

import com.example.bidMarket.dto.Request.ShippingCreateRequest;
import com.example.bidMarket.dto.ShippingDto;

public interface ShippingService {
    public ShippingDto createShipping(ShippingCreateRequest request);
}
