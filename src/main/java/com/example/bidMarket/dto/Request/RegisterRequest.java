package com.example.bidMarket.dto.Request;

import com.example.bidMarket.model.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;
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
