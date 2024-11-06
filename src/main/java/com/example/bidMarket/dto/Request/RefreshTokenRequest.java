package com.example.bidMarket.dto.Request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
