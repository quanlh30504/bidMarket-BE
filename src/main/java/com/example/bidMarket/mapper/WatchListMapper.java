package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.Response.WatchListResponse;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.WatchList;
import com.example.bidMarket.repository.BidRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class WatchListMapper {

    private final BidRepository bidRepository;

    public WatchListResponse watchlistToWatchlistResponse(WatchList watchList, UUID userId) {
        List<ProductImage> productImageList = watchList.getAuction().getProduct().getProductImages();
        Optional<Bid> userBid = bidRepository.findFirstByUserIdAndAuctionIdOrderByBidAmountDesc(userId, watchList.getAuction().getId());

        return WatchListResponse.builder()
                .id(watchList.getId())
                .userId(watchList.getUser().getId())
                .auctionId(watchList.getAuction().getId())
                .auctionTitle(watchList.getAuction().getTitle())
                .productImageUrl(!productImageList.isEmpty() ? productImageList.get(0).getImageUrl() : null)
                .endTime(watchList.getAuction().getEndTime())
                .currentPrice(watchList.getAuction().getCurrentPrice())
                .status(watchList.getAuction().getStatus())
                .bidAmount(userBid.map(Bid::getBidAmount).orElse(null))
                .build();
    }
}