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
@Table(name = "payouts")
public class Payout {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @OneToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payout_status", nullable = false, length = 10)
    private PayoutStatus payoutStatus;

    @Column(name = "payout_initiated_at", columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp payoutInitiatedAt;

    @Column(name = "payout_completed_at", columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp payoutCompletedAt;

    @Column(name = "confirmation_required", columnDefinition = "TINYINT(1) DEFAULT '1'")
    private boolean confirmationRequired;

    @Column(name = "confirmation_received_at", columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp confirmationReceivedAt;

    @Column(name = "auto_payout_due", columnDefinition = "TIMESTAMP DEFAULT NULL")
    private Timestamp autoPayoutDue;


}
