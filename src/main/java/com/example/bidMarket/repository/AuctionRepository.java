package com.example.bidMarket.repository;

import com.example.bidMarket.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {
}
