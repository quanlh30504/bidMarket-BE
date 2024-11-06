package com.example.bidMarket.repository;

import com.example.bidMarket.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
//    @Query(
//            value = "SELECT p FROM Payment p WHERE p.user.id = :id"
//    )
//    List<Payment> findByUserID(@Param("id") UUID id);
//
//    @Query(
//            value = "SELECT p FROM Payment p WHERE p.auction.id = :id"
//    )
//    List<Payment> findByAuctionId(@Param("id") UUID id);

//    Page<Payment> findAllByUserId(UUID userId, Pageable pageable);

}
