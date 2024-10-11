package com.example.bidMarket.model;

import com.example.bidMarket.Enum.PaymentMethod;
import com.example.bidMarket.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.EnableMBeanExport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void preCreate() {
        createdAt = LocalDateTime.now();
    }

}
