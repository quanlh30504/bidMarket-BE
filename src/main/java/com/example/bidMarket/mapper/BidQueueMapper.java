package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.BidQueueDto;
import com.example.bidMarket.model.BidQueue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BidQueueMapper {
    BidQueueDto toDto(BidQueue bidQueue);

    BidQueue toEntity(BidQueueDto bidQueueDto);

    void updateEntity(BidQueueDto bidQueueDto, @MappingTarget BidQueue bidQueue);
}
