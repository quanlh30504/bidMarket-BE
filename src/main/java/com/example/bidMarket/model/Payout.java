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
@Table(name = "payouts")
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auctionId;

    @Column(nullable = false)
    private UUID sellerId;  // Foreign Key to User (seller)

    @Column(nullable = false)
    private UUID buyerId;  // Foreign Key to User (buyer)

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'PENDING'")
    private PayoutStatus payoutStatus = PayoutStatus.PENDING;

    @Column
    private LocalDateTime payoutInitiatedAt;

    @Column
    private LocalDateTime payoutCompletedAt;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean confirmationRequired = true;

    @Column
    private LocalDateTime confirmationReceivedAt;

    @Column
    private LocalDateTime autoPayoutDue;

    // Enum for payout status
    public enum PayoutStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}

