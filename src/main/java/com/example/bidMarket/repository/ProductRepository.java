package com.example.bidMarket.repository;

import com.example.bidMarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}

