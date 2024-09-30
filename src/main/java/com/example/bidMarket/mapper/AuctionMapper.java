package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.model.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuctionMapper {

    //    @Mapping(target = "productId", source = "product.id") // Ánh xạ productId từ product.id
    AuctionDto auctionToAuctionDto(Auction auction);

    //    @Mapping(target = "product.id", source = "productId") // Ánh xạ từ AuctionDto về Auction
    Auction auctionDtoToAuction(AuctionDto auctionDto);

    void updateAuctionFromDto(AuctionDto auctionDto, @MappingTarget Auction auction);
}
