package com.example.bidMarket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_logs")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'PENDING'")
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime initiatedAt = LocalDateTime.now();

    @Column
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String details;

    public enum TransactionType {
        WITHDRAWAL,
        PAYMENT,
        PAYOUT
    }

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}

