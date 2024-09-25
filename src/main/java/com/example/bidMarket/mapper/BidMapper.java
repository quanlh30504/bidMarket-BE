package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.model.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "auctionId", source = "auction.id") // Ánh xạ auctionId từ auction.id
    @Mapping(target = "userId", source = "user.id") // Ánh xạ userId từ user.id
    BidDto bidToBidDto(Bid bid);

    @Mapping(target = "auction.id", source = "auctionId") // Ánh xạ từ BidDto về Bid
    @Mapping(target = "user.id", source = "userId") // Ánh xạ từ BidDto về Bid
    Bid bidDtoToBid(BidDto bidDto);

    void updateBidFromDto(BidDto bidDto, @MappingTarget Bid bid);
}
