package com.example.bidMarket.repository;

import com.example.bidMarket.model.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID>, JpaSpecificationExecutor<Auction> {

    // Optimistic Locking
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Auction> findById(UUID id);

    @Query("SELECT MAX(b.maxBid) FROM Bid b WHERE b.userId = :userId AND b.auction.id = :auctionId")
    BigDecimal findMaxBidByUserAndAuction(@Param("userId") UUID userId, @Param("auctionId") UUID auctionId);
}
