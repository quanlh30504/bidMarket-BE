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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auctionId;

    @Column(nullable = false)
    private UUID buyerId;

    @Column(precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'PENDING'")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column
    private LocalDateTime paymentDue;

    @Column
    private LocalDateTime paymentMadeAt;

    @Column(precision = 10, scale = 2)
    private BigDecimal systemFee;

    // Enum for payment status
    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}
