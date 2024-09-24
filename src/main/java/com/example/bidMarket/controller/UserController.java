package com.example.bidMarket.controller;

import com.example.bidMarket.dto.*;
import com.example.bidMarket.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserDto userDto = userService.createUser(userCreateDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.debug("Received signin request for user: {}", loginRequest.getEmail());
        try {
            JwtAuthenticationResponse response = userService.authenticateUser(loginRequest);
            logger.debug("Authen successful: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authen failed: {}", loginRequest.getEmail());
            throw e;
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtAuthenticationResponse response = userService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        logger.debug("Fetching user with id: {}", id);
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserDto userDto = userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.hasUserId(authentication, #id)")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable UUID id) {
        ProfileDto profileDto = userService.getProfileByUserId(id);
        return ResponseEntity.ok(profileDto);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ProfileDto> updateUserProfile(@PathVariable UUID id,@RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = userService.updateProfile(id, profileDto);
        return ResponseEntity.ok(updatedProfile);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
