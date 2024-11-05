package com.example.bidMarket.service.impl;

import com.example.bidMarket.AWS.AmazonS3Service;
import com.example.bidMarket.dto.*;
import com.example.bidMarket.dto.Request.LoginRequest;
import com.example.bidMarket.dto.Request.RefreshTokenRequest;
import com.example.bidMarket.dto.Request.RegisterRequest;
import com.example.bidMarket.dto.Response.JwtAuthenticationResponse;
import com.example.bidMarket.dto.Response.RegisterResponse;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.RegisterMapper;
import com.example.bidMarket.mapper.UserMapper;
import com.example.bidMarket.model.IdCard;
import com.example.bidMarket.model.Profile;
import com.example.bidMarket.Enum.Role;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.IdCardRepository;
import com.example.bidMarket.repository.ProfileRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.security.JwtTokenProvider;
import com.example.bidMarket.service.ImageService;
import com.example.bidMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationContext applicationContext;
    private final RegisterMapper registerMapper;
    private final ProfileRepository profileRepository;
    private final IdCardRepository idCardRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final AmazonS3Service amazonS3Service;

    @Override
    @Transactional
    public RegisterResponse createUser(RegisterRequest registerRequest) throws Exception{
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        User user = registerMapper.requestToUser(registerRequest);
        user = userRepository.save(user);

        Profile profile = registerMapper.requestToProfile(user, registerRequest);
        profile = profileRepository.save(profile);
        user.setProfile(profile);

        // Handle seller information
        if (user.getRole() == Role.SELLER) {
            IdCard idCard = registerMapper.requestToIdCard(user, registerRequest);
            idCard.setFrontImageURL(registerRequest.getFrontImageURL());
            idCard.setBackImageURL(registerRequest.getBackImageURL());
            idCard = idCardRepository.save(idCard);
            user.setIdCard(idCard);
        }

        user = userRepository.save(user);

        AuthenticationManager authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .jwt(jwt)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUserFromDto(userUpdateDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.userToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {

        userRepository.deleteById(id);
    }

    @Override
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        logger.debug("Authenticating user: {}", loginRequest.getEmail());
        try {
            AuthenticationManager authenticationManager = applicationContext.getBean(AuthenticationManager.class);
            logger.debug("AuthenManager bean retrieved successfully");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);
            return new JwtAuthenticationResponse(jwt, refreshToken);
        } catch (Exception e) {
            logger.error("Unexpected error during authentication", e);
            throw e;
        }
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        boolean isRefreshTokenValid = tokenProvider.validateToken(refreshTokenRequest.getRefreshToken());

        if (isRefreshTokenValid) {
            String userEmail = tokenProvider.getUsernameFromToken(refreshTokenRequest.getRefreshToken());
            UserDetailsService userDetailsService = applicationContext.getBean(UserDetailsService.class);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            String newAccessToken = tokenProvider.generateToken(authenticationToken);
            return new JwtAuthenticationResponse(newAccessToken, refreshTokenRequest.getRefreshToken());
        }

        throw new RuntimeException("Invalid refresh token");
    }

    @Override
    public ProfileDto getProfileByUserId(UUID userId) {
        User user = userRepository.findById((userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.profileToProfileDto(user.getProfile());
    }

    @Override
    public ProfileDto updateProfile(UUID userId, ProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        userMapper.updateProfileFromDto(profileDto, profile);
        User updatedUser = userRepository.save(user);
        return userMapper.profileToProfileDto(updatedUser.getProfile());
    }
}