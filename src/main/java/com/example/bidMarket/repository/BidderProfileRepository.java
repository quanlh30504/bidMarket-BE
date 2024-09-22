package com.example.bidMarket.repository;

import com.example.bidMarket.model.BidderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidderProfileRepository extends JpaRepository<BidderProfile, UUID> {
    Optional<BidderProfile> findByUserId(UUID userId);
}
