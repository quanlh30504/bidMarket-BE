package com.example.bidMarket.controller;

import com.example.bidMarket.dto.MailBody;
import com.example.bidMarket.model.VerifyEmail;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.VerifiyEmailRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.EmailService;
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
    private final VerifiyEmailRepository verifiyEmailRepository;

    public VerifyEmailController(UserRepository userRepository, EmailService emailService, VerifiyEmailRepository verifiyEmailRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.verifiyEmailRepository = verifiyEmailRepository;
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address" + email));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for Forgot Password request")
                .text("This is the OTP for your Forgot Password request: " + otp)
                .build();

        VerifyEmail fp = VerifyEmail.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 20 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        verifiyEmailRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification");
    }

    @PostMapping("/register/{email}")
    public ResponseEntity<String> verifyEmailRegister(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address" + email));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for Verify Email request")
                .text("This is the OTP for your Verify Email request: " + otp)
                .build();

        VerifyEmail fp = VerifyEmail.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 20 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        verifiyEmailRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address"));

        VerifyEmail fp = verifiyEmailRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            verifiyEmailRepository.deleteById(fp.getVeid());
            return new ResponseEntity<>("OTP expired. Please try again", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/forgotPassword/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address"));

        userRepository.updatePassword(user, changePassword.password());

        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/changeStatusVerified/{email}")
    public ResponseEntity<String> changeStatusVerified(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address"));
        userRepository.updateStatus(user);
        return ResponseEntity.ok("Status changed successfully");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}