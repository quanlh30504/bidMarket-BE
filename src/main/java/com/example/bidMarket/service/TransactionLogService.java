package com.example.bidMarket.service;

import com.example.bidMarket.dto.TransactionLogDto;
import com.example.bidMarket.mapper.TransactionLogMapper;
import com.example.bidMarket.model.TransactionLog;
import com.example.bidMarket.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionLogService {

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private TransactionLogMapper transactionLogMapper;

    public TransactionLogDto createTransactionLog(TransactionLogDto transactionLogDto) {
        TransactionLog transactionLog = transactionLogMapper.transactionLogDtoToTransactionLog(transactionLogDto);
        transactionLog = transactionLogRepository.save(transactionLog);
        return transactionLogMapper.transactionLogToTransactionLogDto(transactionLog);
    }

    public TransactionLogDto getTransactionLogById(UUID id) {
        return transactionLogRepository.findById(id)
                .map(transactionLogMapper::transactionLogToTransactionLogDto)
                .orElseThrow(() -> new RuntimeException("TransactionLog not found"));
    }

    public List<TransactionLogDto> getAllTransactionLogs() {
        return transactionLogRepository.findAll().stream()
                .map(transactionLogMapper::transactionLogToTransactionLogDto)
                .collect(Collectors.toList());
    }

    public TransactionLogDto updateTransactionLog(UUID id, TransactionLogDto transactionLogDto) {
        TransactionLog transactionLog = transactionLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TransactionLog not found"));
        transactionLogMapper.updateTransactionLogFromDto(transactionLogDto, transactionLog);
        transactionLog = transactionLogRepository.save(transactionLog);
        return transactionLogMapper.transactionLogToTransactionLogDto(transactionLog);
    }

    public void deleteTransactionLog(UUID id) {
        transactionLogRepository.deleteById(id);
    }
}
