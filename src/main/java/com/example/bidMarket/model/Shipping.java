package com.example.bidMarket.model;

import com.example.bidMarket.Enum.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shippings")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne()
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @ManyToOne()
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne()
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_status", columnDefinition = "ENUM('PENDING','SHIPPED','IN_TRANSIT','DELIVERED')", nullable = false)
    private ShippingStatus status = ShippingStatus.PENDING;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDateTime actualDelivery;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void create(){
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void update(){
        updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    // Constructor
    // Equals and hashCode
}
