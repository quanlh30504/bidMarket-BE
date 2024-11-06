package com.example.bidMarket.controller;

import com.example.bidMarket.dto.*;
import com.example.bidMarket.dto.Request.LoginRequest;
import com.example.bidMarket.dto.Request.RefreshTokenRequest;
import com.example.bidMarket.dto.Request.RegisterRequest;
import com.example.bidMarket.dto.Response.JwtAuthenticationResponse;
import com.example.bidMarket.dto.Response.RegisterResponse;
import com.example.bidMarket.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/signup")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        logger.info("Start sign up for user: {}", registerRequest.getEmail());
        RegisterResponse registerResponse = userService.createUser(registerRequest);
//        createAuthCookies(cookieResponse, registerResponse.getRefreshToken());
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse cookieResponse) {
        logger.debug("Received signin request for user: {}", loginRequest.getEmail());
        try {
            JwtAuthenticationResponse response = userService.authenticateUser(loginRequest);
            createAuthCookies(cookieResponse, response.getRefreshToken());
            logger.debug("Authen successful: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authen failed: {}", loginRequest.getEmail());
            throw e;
        }
    }

    private void createAuthCookies(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // chưa có https
        refreshTokenCookie.setPath("/api/users/refresh-token");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
//        refreshTokenCookie.setSameSite("None");
        response.addCookie(refreshTokenCookie);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        JwtAuthenticationResponse response = userService.refreshToken(new RefreshTokenRequest(refreshToken));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // orther logic -> later
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/api/users/refresh-token");
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok("Logged out successfully.");
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
