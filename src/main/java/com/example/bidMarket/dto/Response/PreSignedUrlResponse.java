package com.example.bidMarket.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreSignedUrlResponse {
    private String uploadUrl;
    private String fileUrl;
}
