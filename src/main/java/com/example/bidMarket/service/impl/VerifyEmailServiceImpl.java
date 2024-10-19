package com.example.bidMarket.service.impl;

import com.example.bidMarket.dto.MailBody;
import com.example.bidMarket.model.VerifyEmail;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.VerifyEmailRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.EmailService;
import com.example.bidMarket.service.VerifyEmailService;
import com.example.bidMarket.utils.ChangePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class VerifyEmailServiceImpl implements VerifyEmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerifyEmailRepository verifyEmailRepository;

    @Override
    public ResponseEntity<String> verifyEmailForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address" + email));

        verifyEmailRepository.findByUser(user).ifPresent(verifyEmailRepository::delete);

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for Forgot Password request")
                .text("This is the OTP for your Forgot Password request: " + otp)
                .build();

        VerifyEmail fp = VerifyEmail.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        verifyEmailRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification");
    }

    @Override
    public ResponseEntity<String> verifyEmailRegister(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address" + email));

        verifyEmailRepository.findByUser(user).ifPresent(verifyEmailRepository::delete);

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for Verify Email request")
                .text("This is the OTP for your Verify Email request: " + otp)
                .build();

        VerifyEmail fp = VerifyEmail.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        verifyEmailRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification");
    }

    @Override
    public ResponseEntity<String> verifyOtpRegister(Integer otp, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address"));

        VerifyEmail fp = verifyEmailRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            verifyEmailRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP expired. Please try again", HttpStatus.EXPECTATION_FAILED);
        }
        userRepository.updateStatus(user); // update user status to verified
        verifyEmailRepository.deleteById(fp.getId());
        return ResponseEntity.ok("OTP verified successfully");
    }

    @Override
    public ResponseEntity<String> verifyOtpForgotPassword(Integer otp, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address"));

        VerifyEmail fp = verifyEmailRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            verifyEmailRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP expired. Please try again", HttpStatus.EXPECTATION_FAILED);
        }

        verifyEmailRepository.deleteById(fp.getId());
        return ResponseEntity.ok("OTP verified successfully");
    }

    @Override
    public ResponseEntity<String> changePasswordHandler(ChangePassword changePassword, String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email address"));

        userRepository.updatePassword(user, changePassword.password());

        return ResponseEntity.ok("Password changed successfully");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}