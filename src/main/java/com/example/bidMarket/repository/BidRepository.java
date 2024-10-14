package com.example.bidMarket.repository;


import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {

//    @Query("SELECT b FROM Bid b WHERE b.auction.id = :auctionId AND b.status = 'VALID' ORDER BY b.bidAmount DESC")
//    Optional<Bid> findHighestValidBidByAuctionId(@Param("auctionId") UUID auctionId);
    Optional<Bid> findFirstByAuctionIdAndStatusOrderByBidAmountDesc(UUID auctionId, BidStatus status);

}
