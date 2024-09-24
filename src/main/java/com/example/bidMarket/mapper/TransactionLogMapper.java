package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.TransactionLogDto;
import com.example.bidMarket.model.TransactionLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionLogMapper {

    // Ánh xạ từ TransactionLog entity sang TransactionLogDto
    TransactionLogDto transactionLogToTransactionLogDto(TransactionLog transactionLog);

    // Ánh xạ từ TransactionLogDto sang TransactionLog entity
    TransactionLog transactionLogDtoToTransactionLog(TransactionLogDto transactionLogDto);

    // Cập nhật thông tin từ TransactionLogDto vào TransactionLog entity
    void updateTransactionLogFromDto(TransactionLogDto transactionLogDto, @MappingTarget TransactionLog transactionLog);
}
