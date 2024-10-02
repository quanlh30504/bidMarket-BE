package com.example.bidMarket.model;

import com.example.bidMarket.Enum.BidStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@Entity
@Table(name = "bids")
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "bid_amount")
    private BigDecimal bidAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BidStatus status;

    @CreatedDate
    @Column(name = "bid_time")
    private LocalDateTime bidTime;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
