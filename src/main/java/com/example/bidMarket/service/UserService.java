package com.example.bidMarket.service;

import com.example.bidMarket.dto.*;
import com.example.bidMarket.dto.Request.LoginRequest;
import com.example.bidMarket.dto.Request.RefreshTokenRequest;
import com.example.bidMarket.dto.Request.RegisterRequest;
import com.example.bidMarket.dto.Response.JwtAuthenticationResponse;
import com.example.bidMarket.dto.Response.RegisterResponse;

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

    ProfileDto getProfileByUserId(UUID userId);
    ProfileDto updateProfile(UUID userId, ProfileDto profileDto);
}