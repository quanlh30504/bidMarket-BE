package com.example.bidMarket.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transaction_logs")
public class TransactionLog {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", columnDefinition = "ENUM('PENDING','COMPLETED','FAILED') DEFAULT 'PENDING'")
    private TransactionStatus transactionStatus;

    @Column(name = "initiated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp initiatedAt;

    @Column(name = "completed_at", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private Timestamp completedAt;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

}
