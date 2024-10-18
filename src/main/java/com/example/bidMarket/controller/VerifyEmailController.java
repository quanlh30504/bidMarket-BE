package com.example.bidMarket.controller;

import com.example.bidMarket.dto.MailBody;
import com.example.bidMarket.model.VerifyEmail;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.VerifyEmailRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.EmailService;
import com.example.bidMarket.service.VerifyEmailService;
import com.example.bidMarket.utils.ChangePassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/verifyEmail")
public class VerifyEmailController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerifyEmailRepository verifyEmailRepository;
    private final VerifyEmailService verifyEmailService;

    public VerifyEmailController(UserRepository userRepository, EmailService emailService,
                                 VerifyEmailRepository verifyEmailRepository, VerifyEmailService verifyEmailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.verifyEmailRepository = verifyEmailRepository;
        this.verifyEmailService = verifyEmailService;
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<String> verifyEmailForgotPassword(@PathVariable String email) {
        return verifyEmailService.verifyEmailForgotPassword(email);
    }

    @PostMapping("/register/{email}")
    public ResponseEntity<String> verifyEmailRegister(@PathVariable String email) {
        return verifyEmailService.verifyEmailRegister(email);
    }

    @PostMapping("/forgotPassword/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtpForgotPassword(@PathVariable Integer otp, @PathVariable String email) {
        return verifyEmailService.verifyOtp(otp, email);
    }

    @PostMapping("/forgotPassword/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email) {
        return verifyEmailService.changePasswordHandler(changePassword, email);
    }

}