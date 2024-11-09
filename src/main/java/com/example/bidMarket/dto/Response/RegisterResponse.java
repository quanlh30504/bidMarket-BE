package com.example.bidMarket.dto.Response;

import com.example.bidMarket.Enum.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class RegisterResponse {
    private UUID id;
    private String email;
    private Role role;
    private String fullName;
    private String phoneNumber;

    private LocalDate issuedDate;
    private LocalDate expirationDate;
//    private String jwt;
//    private String refreshToken;
}
