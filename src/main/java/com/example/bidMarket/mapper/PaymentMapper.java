package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "auctionId", source = "auction.id")
    @Mapping(target = "userId", source = "user.id")
    PaymentDto paymentToPaymentDto(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auction",ignore = true)
    @Mapping(target = "user", ignore = true)
    Payment paymentDtoToPayment(PaymentDto paymentDto);
}
