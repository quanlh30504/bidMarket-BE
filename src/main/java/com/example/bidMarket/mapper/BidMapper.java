package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.model.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "auctionId", source = "auction.id")
    BidDto bidToBidDto(Bid bid);
}
