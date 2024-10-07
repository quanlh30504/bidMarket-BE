package com.example.bidMarket.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ProductImageDto {
    private String imageUrl;
    private Boolean isPrimary;

}
