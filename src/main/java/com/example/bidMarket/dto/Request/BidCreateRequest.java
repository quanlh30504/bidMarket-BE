package com.example.bidMarket.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
public class BidCreateRequest {
    private UUID userId;
    private UUID auctionId;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
}
