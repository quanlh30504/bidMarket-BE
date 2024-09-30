package com.example.bidMarket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionLogDto {
    private UUID id;
    private UUID userId;
    private String transactionType;  // Loại giao dịch (WITHDRAWAL, PAYMENT, PAYOUT)
    private BigDecimal amount;
    private String transactionStatus;  // Trạng thái (PENDING, COMPLETED, FAILED)
    private Timestamp initiatedAt;
    private Timestamp completedAt;  // Chỉ có giá trị nếu transactionStatus là COMPLETED
    private String details;
}