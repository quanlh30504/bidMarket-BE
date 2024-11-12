package com.example.bidMarket.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
//    AUCTION_START("Auction Started"),
//    AUCTION_END("Auction Ended"),
//    NEW_BID("New Bid Placed"),
//    OUTBID("Outbid Alert"),
//    BID_WIN("Winning Bid"),
//    AUCTION_EXTENDED("Auction Extended"),
//    AUCTION_CANCELLED("Auction Cancelled"),
//    AUCTION_COMPLETED("Auction Completed"),
//    PRICE_UPDATE("Price Updated");
    SUCCESS("success"),
    ERROR("error"),
    INFO("info"),
    WARNING("warning");
    private final String displayName;
}