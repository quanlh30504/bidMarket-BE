package com.example.bidMarket.service;

import com.example.bidMarket.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserCreateDto userCreateDto);
    UserDto getUserById(UUID id);
    List<UserDto> getAllUsers();
    UserDto updateUser(UUID id, UserUpdateDto userUpdateDto);
    void deleteUser(UUID id);
    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    ProfileDto getProfileByUserId(UUID userId);
    ProfileDto updateProfile(UUID userId, ProfileDto profileDto);
}
