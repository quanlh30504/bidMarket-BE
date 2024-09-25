package com.example.bidMarket.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bid_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal bidAmount;

    @Column(name = "bid_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp bidTime;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private BidStatus status;

}
