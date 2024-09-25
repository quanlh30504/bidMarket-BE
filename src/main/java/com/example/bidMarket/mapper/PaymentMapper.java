package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "auctionId", source = "auction.id") // ánh xạ auctionId
    PaymentDto toDto(Payment payment);

    @Mapping(target = "auction.id", source = "auctionId") // ánh xạ auctionId

    Payment toEntity(PaymentDto paymentDto);

    void updateEntity(PaymentDto paymentDto, @MappingTarget Payment payment);
}
