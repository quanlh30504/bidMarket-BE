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
@Table(name = "bid_queues")
public class BidQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID auctionId;  // Foreign Key to Auction

    @Column(nullable = false)
    private UUID userId;  // Foreign Key to User

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bidAmount;

    @Column(nullable = false)
    private LocalDateTime bidTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status;


    public enum BidStatus {
        PENDING,
        PROCESSED,
        FAILED
    }
}

