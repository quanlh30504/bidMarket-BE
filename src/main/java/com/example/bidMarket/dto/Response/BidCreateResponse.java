package com.example.bidMarket.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BidCreateResponse {
    private UUID id;
}
