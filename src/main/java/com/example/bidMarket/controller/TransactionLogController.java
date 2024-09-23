package com.example.bidMarket.controller;

import com.example.bidMarket.dto.TransactionLogDto;
import com.example.bidMarket.service.TransactionLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction-logs")
public class TransactionLogController {
    private final TransactionLogService transactionLogService;

    public TransactionLogController(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionLogDto>> getTransactionLogsByUserId(@PathVariable UUID userId) {
        List<TransactionLogDto> transactionLogs = transactionLogService.getTransactionLogsByUserId(userId);
        return ResponseEntity.ok(transactionLogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionLogDto> getTransactionLogById(@PathVariable UUID id) {
        TransactionLogDto transactionLogDto = transactionLogService.getTransactionLogById(id);
        return ResponseEntity.ok(transactionLogDto);
    }

    @PostMapping
    public ResponseEntity<TransactionLogDto> createTransactionLog(@RequestBody TransactionLogDto transactionLogDto) {
        TransactionLogDto createdLog = transactionLogService.createTransactionLog(transactionLogDto);
        return ResponseEntity.ok(createdLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable UUID id) {
        transactionLogService.deleteTransactionLog(id);
        return ResponseEntity.noContent().build();
    }
}
