package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
    private String fullName;
    private String phoneNumber;

    // Only apply if role is SELLER
    private String idCard;
    private MultipartFile frontImage;
    private MultipartFile backImage;
    private LocalDate issuedDate;
    private LocalDate expirationDate;
}
