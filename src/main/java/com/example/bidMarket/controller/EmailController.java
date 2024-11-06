package com.example.bidMarket.controller;

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
@RequestMapping("/verifyEmail")
@Slf4j
public class EmailController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerifyEmailRepository verifiyEmailRepository;
    private final VerifyEmailService verifyEmailService;

    public EmailController(UserRepository userRepository, EmailService emailService,
                           VerifyEmailRepository verifiyEmailRepository, VerifyEmailService verifyEmailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.verifiyEmailRepository = verifiyEmailRepository;
        this.verifyEmailService = verifyEmailService;
    }
    // send opt when forgot password
    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<String> verifyEmailForgotPassword(@PathVariable String email) {
        return verifyEmailService.verifyEmailForgotPassword(email);
    }

    // send opt to verify email when register new account
    @PostMapping("/register/{email}")
    public ResponseEntity<String> verifyEmailRegister(@PathVariable String email) {
        return verifyEmailService.verifyEmailAndSendOtp(email);
    }

    @PostMapping("/register/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        return verifyEmailService.verifyOtp(otp, email);
    }

    @PostMapping("/forgotPassword/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtpForgotPassword(@PathVariable Integer otp, @PathVariable String email) {
        return verifyEmailService.verifyOtpForgotPassword(otp, email);
    }

    @GetMapping("/reSendOtp/{email}")
    public ResponseEntity<String> resendOtp(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().isVerified()){
            log.info("Email was verified");
            throw new AppException(ErrorCode.USER_WAS_VERIFIED);
        }
        return ResponseEntity.ok("New otp: " + verifyEmailService.verifyEmailAndSendOtp(email));
    }

}