package com.example.bidMarket.service.impl;

import com.example.bidMarket.dto.*;
import com.example.bidMarket.mapper.UserMapper;
import com.example.bidMarket.model.Profile;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.security.JwtTokenProvider;
import com.example.bidMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationContext applicationContext;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider,
                           ApplicationContext applicationContext) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.applicationContext = applicationContext;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    @Transactional
    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        user.setPasswordHash(passwordEncoder.encode(userCreateDto.getPassword()));

        Profile profile = new Profile();
        profile.setUser(user);
        user.setProfile(profile);

        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .orElseThrow(() -> new RuntimeException("User not found"));

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
