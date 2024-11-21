package com.example.bidMarket.service;

import com.example.bidMarket.Enum.Role;
import com.example.bidMarket.dto.*;
import com.example.bidMarket.dto.Request.LoginRequest;
import com.example.bidMarket.dto.Request.RefreshTokenRequest;
import com.example.bidMarket.dto.Request.RegisterRequest;
import com.example.bidMarket.dto.Response.AccountInfo;
import com.example.bidMarket.dto.Response.JwtAuthenticationResponse;
import com.example.bidMarket.dto.Response.RegisterResponse;
import com.example.bidMarket.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {
    RegisterResponse createUser(RegisterRequest registerRequest) throws Exception;
    UserDto getUserById(UUID id);
    List<UserDto> getAllUsers();
    UserDto updateUser(UUID id, UserUpdateDto userUpdateDto);
    void deleteUser(UUID id);
    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void changePassword(String email, String currentPassword, String newPassword);
    void changePasswordForgot(String newPassword, String email);
    ProfileDto getProfileByUserId(UUID userId);
    ProfileDto updateProfile(UUID userId, ProfileDto profileDto);
    void updateAvatar(UUID userId, String imageUrl);
    AccountInfo getAccountInfoByUserId(UUID userId);

    public Page<User> searchUsers(String email, Role role, Boolean isBanned, Boolean isVerified,
                                  int page, int size, String sortBy, String sortDirection);

}
