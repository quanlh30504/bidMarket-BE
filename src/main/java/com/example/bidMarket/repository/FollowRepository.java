package com.example.bidMarket.repository;

import com.example.bidMarket.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    boolean existsByFollowerIdAndSellerId(UUID followerId, UUID sellerId);
    Optional<Follow> findByFollowerIdAndSellerId(UUID followerId, UUID sellerId);
}