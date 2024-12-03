package com.example.bidMarket.repository;

import com.example.bidMarket.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query(
            "select a from Address a where a.user.id = :userId"
    )
    Optional<Address> findByUserId(@Param("userId") UUID userId);
}
