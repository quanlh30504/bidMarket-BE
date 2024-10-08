package com.example.bidMarket.SearchService;


import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Category;
import com.example.bidMarket.model.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionSpecification {

    public static Specification<Auction> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title.trim() + "%");
    }

    public static Specification<Auction> hasCategoryType(CategoryType categoryType) {
        return (root, query, criteriaBuilder) -> {
            if (categoryType == null) {
                return null;
            }
            // Join với bảng product và category
            Join<Auction, Product> productJoin = root.join("product");
            Join<Product, Category> categoryJoin = productJoin.join("categories");
            return criteriaBuilder.equal(categoryJoin.get("categoryType"), categoryType);
        };
    }

    public static Specification<Auction> hasStatus(AuctionStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Auction> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("currentPrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("currentPrice"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("currentPrice"), maxPrice);
            } else {
                return null;
            }
        };
    }

    public static Specification<Auction> hasStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return (root, query, criteriaBuilder) -> {
            if (startTime != null && endTime != null) {
                return criteriaBuilder.between(root.get("startTime"), startTime, endTime);
            } else if (startTime != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime);
            } else if (endTime != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), endTime);
            } else {
                return null;
            }
        };
    }
}