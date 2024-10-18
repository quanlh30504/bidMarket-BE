package com.example.bidMarket.service;

import com.example.bidMarket.dto.MailBody;
import com.example.bidMarket.model.VerifyEmail;
import com.example.bidMarket.model.User;
import com.example.bidMarket.utils.ChangePassword;
import org.springframework.http.ResponseEntity;

public interface VerifyEmailService {
    ResponseEntity<String> verifyEmailForgotPassword(String email);
    ResponseEntity<String> verifyEmailRegister(String email);
    ResponseEntity<String> verifyOtp(Integer otp, String email);
    ResponseEntity<String> changePasswordHandler(ChangePassword changePassword, String email);

}