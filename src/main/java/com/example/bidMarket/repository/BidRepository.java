package com.example.bidMarket.repository;

import com.example.bidMarket.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    Optional<Bid> findByAuctionId(UUID auctionId);
}