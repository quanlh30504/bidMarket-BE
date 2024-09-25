package com.example.bidMarket.service.impl;

import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user;
        try {
            UUID userId = UUID.fromString(email);
            user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("User not found with id: {}", userId);
                        return new UsernameNotFoundException("User not found with id: " + userId);
                    });
        } catch (IllegalArgumentException e) {
            // If username is not a valid UUID, assume it's an email
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("User not found with email: {}", email);
                        return new UsernameNotFoundException("User not found with email: " + email);
                    });
        }


        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
