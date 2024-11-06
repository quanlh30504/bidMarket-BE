package com.example.bidMarket.model;

import com.example.bidMarket.Enum.AuctionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@DynamicUpdate
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @OneToOne(fetch = FetchType.EAGER)
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

    @Column(name = "last_bid_time", nullable = false)
    private LocalDateTime lastBidTime;

    @Column(name = "extension_count", nullable = false)
    private int extensionCount  = 0;

    @Version
    private int version;

    @Column(name = "bid_count", nullable = false)
    private long bidCount = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, insertable = false)
    private LocalDateTime updatedAt;

}
