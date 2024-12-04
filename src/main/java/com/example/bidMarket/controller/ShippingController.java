package com.example.bidMarket.controller;

import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.Response.ShippingResponse;
import com.example.bidMarket.dto.ShippingDto;
import com.example.bidMarket.mapper.ShippingMapper;
import com.example.bidMarket.model.Shipping;
import com.example.bidMarket.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;
    private final ShippingMapper shippingMapper;

    @GetMapping("/buyer/{buyerId}")
    public PaginatedResponse<ShippingResponse> getShippingByBuyer(@PathVariable UUID buyerId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "createdAt") String sortField,
                                                             @RequestParam(defaultValue = "DESC") String sortDirection

    ){
        Page<Shipping> shippings = shippingService.getShippingByBuyer(buyerId,page, size, sortField, sortDirection);
        List<ShippingResponse> content = shippings.getContent().stream()
                .map(shippingMapper::shippingToShippingResponse)
                .toList();
        return new PaginatedResponse<>(
                shippings.getNumber(),
                shippings.getSize(),
                shippings.getTotalElements(),
                shippings.getTotalPages(),
                shippings.isLast(),
                shippings.isFirst(),
                content
        );

    }
    @GetMapping("/seller/{sellerId}")
    public PaginatedResponse<ShippingResponse> getShippingBySeller(@PathVariable UUID sellerId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "createdAt") String sortField,
                                                                  @RequestParam(defaultValue = "DESC") String sortDirection

    ) {
        Page<Shipping> shippings = shippingService.getShippingBuySeller(sellerId, page, size, sortField, sortDirection);
        List<ShippingResponse> content = shippings.getContent().stream()
                .map(shippingMapper::shippingToShippingResponse)
                .toList();
        return new PaginatedResponse<>(
                shippings.getNumber(),
                shippings.getSize(),
                shippings.getTotalElements(),
                shippings.getTotalPages(),
                shippings.isLast(),
                shippings.isFirst(),
                content
        );
    }


}
