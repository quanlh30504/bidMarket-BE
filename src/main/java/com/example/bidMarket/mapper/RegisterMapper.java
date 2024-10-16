package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.Request.RegisterRequest;
import com.example.bidMarket.dto.Response.RegisterResponse;
import com.example.bidMarket.model.IdCard;
import com.example.bidMarket.model.Profile;
import com.example.bidMarket.Enum.Role;
import com.example.bidMarket.model.User;
import com.mysql.cj.protocol.x.XProtocolRowInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterMapper {
    private final PasswordEncoder passwordEncoder;

    @Value("${aws.default_avatar_url}")
    private String DEFAULT_IMAGE_URL;
    public User requestToUser(RegisterRequest request) {
        return User.builder()
                .role(request.getRole())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    public Profile requestToProfile(User user, RegisterRequest request) {
        return Profile.builder()
                .user(user)  // Gán user đã tạo trước đó
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .profileImageUrl(DEFAULT_IMAGE_URL) // AVATAR DEFAULT
                .build();
    }

    public IdCard requestToIdCard(User user, RegisterRequest request) {
        return IdCard.builder()
                .user(user)  // Gán user đã tạo trước đó
                .idCard(request.getIdCard())
//                .frontImageURL(request.getFrontImage())
//                .backImageURL(request.getBackImage())
                .issuedDate(request.getIssuedDate())
                .expirationDate(request.getExpirationDate())
                .build();
    }

    public RegisterResponse toRegisterResponse(User user) {
        RegisterResponse registerResponse = RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .fullName(user.getProfile().getFullName())
                .phoneNumber(user.getProfile().getPhoneNumber())
                .profileImageUrl(user.getProfile().getProfileImageUrl()).build();

        if (user.getRole() == Role.SELLER && user.getIdCard() != null) {
            registerResponse.setIdCard(user.getIdCard().getIdCard());
            registerResponse.setFrontImageURL(user.getIdCard().getFrontImageURL());
            registerResponse.setBackImageURL(user.getIdCard().getBackImageURL());
            registerResponse.setIssuedDate(user.getIdCard().getIssuedDate());
            registerResponse.setExpirationDate(user.getIdCard().getExpirationDate());
        }

        return registerResponse;
    }

}
