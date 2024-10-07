package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
    private String fullName;
    private String phoneNumber;
    private MultipartFile profileImageUrl;

    // Chỉ áp dụng nếu role là SELLER
    private String idCard;
    private MultipartFile frontImage;
    private MultipartFile backImage;
    private LocalDate issuedDate;
    private LocalDate expirationDate;
}
