package com.example.bidMarket.controller;

import com.example.bidMarket.MQTemplate.EmailProvider;
import com.example.bidMarket.dto.Request.EmailRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.repository.VerifyEmailRepository;
import com.example.bidMarket.service.EmailService;
import com.example.bidMarket.service.VerifyEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/emails")
@Slf4j
public class EmailController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerifyEmailRepository verifiyEmailRepository;
    private final VerifyEmailService verifyEmailService;
    private final EmailProvider emailProvider;

    public EmailController(UserRepository userRepository, EmailService emailService,
                           VerifyEmailRepository verifiyEmailRepository, VerifyEmailService verifyEmailService, EmailProvider emailProvider) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.verifiyEmailRepository = verifiyEmailRepository;
        this.verifyEmailService = verifyEmailService;
        this.emailProvider = emailProvider;
    }

    // send opt to verify email
    @PostMapping("/sendOtp/{email}")
    public ResponseEntity<String> sendOtp(@PathVariable String email) {
        log.info("Sent opt to email: " + email);
        emailProvider.sendEmailOTPRequest(EmailRequest.builder().email(email).build());
        return ResponseEntity.ok("Send OTP successfully");
    }

    @PostMapping("/register/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        return verifyEmailService.verifyOtp(otp, email);
    }

    @PostMapping("/forgotPassword/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtpForgotPassword(@PathVariable Integer otp, @PathVariable String email) {
        return verifyEmailService.verifyOtpForgotPassword(otp, email);
    }
}