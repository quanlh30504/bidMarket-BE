package com.example.bidMarket.model;

import com.example.bidMarket.Enum.AuctionStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "current_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "starting_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal startingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionStatus status = AuctionStatus.PENDING;

    @Column(name = "minimum_bid_increment", precision = 10, scale = 2, nullable = false)
    private BigDecimal minimumBidIncrement;

    @Column(name = "extension_count", nullable = false)
    private int extensionCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    private LocalDateTime updatedAt;


}
