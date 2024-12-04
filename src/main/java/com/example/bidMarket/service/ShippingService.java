package com.example.bidMarket.service;

import com.example.bidMarket.dto.Request.ShippingCreateRequest;
import com.example.bidMarket.dto.ShippingDto;
import com.example.bidMarket.model.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface ShippingService {
    public ShippingDto createShipping(ShippingCreateRequest request);

    public Page<Shipping> getShippingByBuyer(UUID buyerId, int page, int size, String sortField, String sortDirection);

    public Page<Shipping> getShippingBuySeller(UUID sellerId, int page, int size, String sortField, String sortDirection);
}
