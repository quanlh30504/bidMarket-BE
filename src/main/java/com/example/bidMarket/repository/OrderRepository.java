package com.example.bidMarket.repository;

import com.example.bidMarket.model.Order;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> , JpaSpecificationExecutor<Order> {

    @Query(
            "Select o from Order o where o.auction.id = :auctionId"
    )
    Optional<Order> findByAuctionId(@Param("auctionId") UUID auctionId);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    @Query(
            "Select o from Order o where o.auction.product.seller.id = :sellerId"
    )
    Page<Order> findBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);
}
