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

    @PostMapping
    public ResponseEntity<TransactionLogDto> createTransactionLog(@RequestBody TransactionLogDto transactionLogDto) {
        TransactionLogDto createdTransactionLog = transactionLogService.createTransactionLog(transactionLogDto);
        return ResponseEntity.ok(createdTransactionLog);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionLogDto> getTransactionLogById(@PathVariable UUID id) {
        TransactionLogDto transactionLogDto = transactionLogService.getTransactionLogById(id);
        return ResponseEntity.ok(transactionLogDto);
    }

    @GetMapping
    public ResponseEntity<List<TransactionLogDto>> getAllTransactionLogs() {
        List<TransactionLogDto> transactionLogs = transactionLogService.getAllTransactionLogs();
        return ResponseEntity.ok(transactionLogs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionLogDto> updateTransactionLog(@PathVariable UUID id, @RequestBody TransactionLogDto transactionLogDto) {
        TransactionLogDto updatedTransactionLog = transactionLogService.updateTransactionLog(id, transactionLogDto);
        return ResponseEntity.ok(updatedTransactionLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionLog(@PathVariable UUID id) {
        transactionLogService.deleteTransactionLog(id);
        return ResponseEntity.ok().build();
    }
}
