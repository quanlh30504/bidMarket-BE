package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.Response.WatchListResponse;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.WatchList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class WatchListMapper {
    public static WatchListResponse watchlistToWatchlistResponse(WatchList watchList){
        List<ProductImage> productImageList = watchList.getAuction().getProduct().getProductImages();
        return WatchListResponse.builder()
                .id(watchList.getId())
                .userId(watchList.getUser().getId())
                .auctionId(watchList.getAuction().getId())
                .auctionTitle(watchList.getAuction().getTitle())
                .productImageUrl(!productImageList.isEmpty() ? productImageList.get(0).getImageUrl() : null)
                .endTime(watchList.getAuction().getEndTime())
                .currentPrice(watchList.getAuction().getCurrentPrice())
                .status(watchList.getAuction().getStatus())
//                .bidCount()
                .build();
    }
}
