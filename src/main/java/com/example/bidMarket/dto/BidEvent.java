package com.example.bidMarket.dto;

import lombok.*;

@Builder
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BidEvent {
    private String action;
    private Object data;
}
