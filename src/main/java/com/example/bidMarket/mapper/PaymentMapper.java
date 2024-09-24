package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);

    Payment toEntity(PaymentDto paymentDto);

    void updateEntity(PaymentDto paymentDto, @MappingTarget Payment payment);
}
