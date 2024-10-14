package com.example.bidMarket.repository;

import com.example.bidMarket.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    @Modifying
    @Query(
            "DELETE FROM ProductImage pi where pi.product.id = :productId"
    )
    void deleteByProductId(@Param("productId") UUID productId);
}
