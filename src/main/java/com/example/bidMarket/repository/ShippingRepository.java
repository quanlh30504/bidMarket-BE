package com.example.bidMarket.repository;

import com.example.bidMarket.model.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, UUID> {
    Page<Shipping> findByBuyerId(UUID buyerId, Pageable pageable);
    Page<Shipping> findBySellerId(UUID sellerId, Pageable pageable);
}
