package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.ShippingDto;
import com.example.bidMarket.model.Shipping;
import org.springframework.stereotype.Component;

@Component
public class ShippingMapper {
    public ShippingDto shippingToShippingDto(Shipping shipping){
        return ShippingDto.builder()
                .id(shipping.getId())
                .auctionId(shipping.getAuction().getId())
                .buyerId(shipping.getBuyer().getId())
                .sellerId(shipping.getSeller().getId())
                .trackingNumber(shipping.getTrackingNumber())
                .shippedAt(shipping.getShippedAt())
                .estimatedDelivery(shipping.getEstimatedDelivery())
                .actualDelivery(shipping.getActualDelivery())
                .build();
    }
}
