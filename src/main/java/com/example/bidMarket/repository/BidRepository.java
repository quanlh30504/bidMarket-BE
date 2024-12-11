package com.example.bidMarket.repository;


import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.model.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    Optional<Bid> findFirstByUserIdAndAuctionIdOrderByBidAmountDesc(UUID userId, UUID auctionId);

    Optional<Bid> findFirstByAuctionIdAndStatusOrderByBidAmountDesc(UUID auctionId, BidStatus status);

    Page<Bid> findAllByAuctionId(UUID auctionId, Pageable pageable);

    Page<Bid> findAllByAuctionIdAndStatus(UUID auctionId, BidStatus status, Pageable pageable);

    Optional<Bid> findTopByAuctionIdOrderByBidAmountDesc(UUID auctionId);
    Optional<Bid> findFirstByAuctionIdOrderByBidAmountDesc(UUID auctionId);
    long countByAuctionIdAndStatus(UUID auctionId, BidStatus status);
    List<Bid> findByAuctionId(UUID auctionId);
    List<Bid> findByAuctionIdAndStatus(UUID id, BidStatus bidStatus);
}
