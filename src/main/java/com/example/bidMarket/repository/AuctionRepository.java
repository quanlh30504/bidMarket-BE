package com.example.bidMarket.repository;

import com.example.bidMarket.model.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {

    // Optimistic Locking
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Auction> findById(UUID id);
}
