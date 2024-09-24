package com.example.bidMarket.repository;

import com.example.bidMarket.model.BidQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface BidQueueRepository extends JpaRepository<BidQueue, UUID> {
    Optional<BidQueue> findByAuctionId(UUID auctionId);
}
