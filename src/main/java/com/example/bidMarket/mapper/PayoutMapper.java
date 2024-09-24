package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.PayoutDto;
import com.example.bidMarket.model.Payout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayoutMapper {
    @Mapping(target = "auctionId", source = "auction") // Ánh xạ từ auction.id sang auctionId
    PayoutDto payoutToPayoutDto(Payout payout);

    @Mapping(target = "auction", source = "auctionId") // Ánh xạ từ auctionId về auction.id
    Payout payoutDtoToPayout(PayoutDto payoutDto);
}

