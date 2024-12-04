package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.Response.ShippingResponse;
import com.example.bidMarket.dto.ShippingDto;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.Shipping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShippingMapper {
    public ShippingDto shippingToShippingDto(Shipping shipping){
        return ShippingDto.builder()
                .id(shipping.getId())
                .auctionId(shipping.getAuction().getId())
                .buyerId(shipping.getBuyer().getId())
                .sellerId(shipping.getSeller().getId())
                .price(shipping.getPrice())
                .trackingNumber(shipping.getTrackingNumber())
                .shippedAt(shipping.getShippedAt())
                .estimatedDelivery(shipping.getEstimatedDelivery())
                .actualDelivery(shipping.getActualDelivery())
                .build();
    }

    public ShippingResponse shippingToShippingResponse(Shipping shipping){
        List<ProductImage> productImageList = shipping.getAuction().getProduct().getProductImages();
//        String productImageUrl = null;
//        for (int i = 0; i < productImageList.size(); i++) {
//            if (productImageList.get(i).isPrimary()) {
//                productImageUrl = productImageList.get(i).getImageUrl();
//                break;
//            }
//        }
        return ShippingResponse.builder()
                .id(shipping.getId())
                .auctionId(shipping.getAuction().getId())
                .buyerId(shipping.getBuyer().getId())
                .sellerId(shipping.getSeller().getId())
                .productName(shipping.getAuction().getProduct().getName())
                .productImageUrl(!productImageList.isEmpty()
                        ? productImageList
                        .stream()
                        .filter(ProductImage::isPrimary)
                        .findFirst()
                        .map(ProductImage::getImageUrl)
                        .orElse(productImageList.get(0).getImageUrl())
                        : null)
                .shopName(shipping.getSeller().getProfile().getFullName())
                .quantity(1)
                .price(shipping.getPrice())
                .status(shipping.getStatus())
                .build();
    }
}
