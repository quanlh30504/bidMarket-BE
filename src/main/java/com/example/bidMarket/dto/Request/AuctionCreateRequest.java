package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.User;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionCreateRequest {
    private AuctionDto auctionDto;

    // About product
    private ProductDto productDto;

    // About productImage;
    private ProductImageDto productImageDto;

}
