package com.example.bidMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bidMarket.model.Auction;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {
}