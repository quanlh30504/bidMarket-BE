package com.example.bidMarket.service;

import org.springframework.http.ResponseEntity;

public interface VerifyEmailService {
    ResponseEntity<String> sendOtp(String email);
    ResponseEntity<String> verifyOtp(Integer otp, String email);
    ResponseEntity<String> verifyOtpForgotPassword(Integer otp, String email);
}