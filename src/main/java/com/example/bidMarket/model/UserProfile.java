package com.example.bidMarket.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class UserProfile {
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl;
}
