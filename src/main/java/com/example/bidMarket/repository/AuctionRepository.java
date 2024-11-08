package com.example.bidMarket.repository;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.model.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID>, JpaSpecificationExecutor<Auction> {

    // Optimistic Locking
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Auction> findById(UUID id);

    List<Auction> findByEndTimeBeforeAndStatus(LocalDateTime now, AuctionStatus status);

    @Query("SELECT a FROM Auction a WHERE a.product.seller.id = :userId")
    Page<Auction> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);
}
