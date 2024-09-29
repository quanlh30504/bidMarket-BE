package com.example.bidMarket.dto.Response;

import com.example.bidMarket.dto.IdCardDto;
import com.example.bidMarket.dto.ProfileDto;
import com.example.bidMarket.model.Role;
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
    private String profileImageUrl;

    // Chỉ áp dụng nếu role là SELLER
    private String idCard;
    private String frontImageURL;
    private String backImageURL;
    private LocalDate issuedDate;
    private LocalDate expirationDate;
}
