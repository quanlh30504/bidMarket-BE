package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.model.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BidMapper {
    @Mapping(target = "auctionId", source = "auction.id") // ánh xạ auctionId
    BidDto bidToBidDto(Bid bid);

    @Mapping(target = "auction.id", source = "auctionId") // ánh xạ từ BidDto về Bid
    Bid bidDtoToBid(BidDto bidDto);

    void updateBidFromDto(BidDto bidDto, @MappingTarget Bid bid);
}
