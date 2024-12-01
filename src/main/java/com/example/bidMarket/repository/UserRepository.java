package com.example.bidMarket.repository;

import com.example.bidMarket.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> , JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.passwordHash = ?2 where u = ?1")
    void updatePassword(User user, String password);

    @Transactional
    @Modifying
    @Query("update User u set u.isVerified = true where u = ?1")
    void updateStatus(User user);
}