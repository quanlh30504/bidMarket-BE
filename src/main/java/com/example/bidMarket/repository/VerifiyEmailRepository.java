package com.example.bidMarket.repository;

import com.example.bidMarket.model.VerifyEmail;
import com.example.bidMarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VerifiyEmailRepository extends JpaRepository<VerifyEmail, Integer> {
    @Query("select ve from VerifyEmail ve where ve.otp = ?1 and ve.user = ?2")
    Optional<VerifyEmail> findByOtpAndUser(Integer otp, User user);
}
