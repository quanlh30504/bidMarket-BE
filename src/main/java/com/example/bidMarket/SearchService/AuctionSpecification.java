package com.example.bidMarket.SearchService;


import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Category;
import com.example.bidMarket.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AuctionSpecification {

    public static Specification<Auction> hasSellerId(UUID sellerId) {
        return ((root, query, criteriaBuilder) ->
                sellerId == null ? null : criteriaBuilder.equal(root.get("product").get("seller").get("id"), sellerId)
        );
    }

    public static Specification<Auction> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title.trim() + "%");
    }

    public static Specification<Auction> hasCategoryTypes(List<CategoryType> categoryTypes) {
        return (root, query, criteriaBuilder) -> {
            if (categoryTypes == null || categoryTypes.isEmpty()) {
                return null;
            }

            // Join với bảng product và category
            Join<Auction, Product> productJoin = root.join("product");
            Join<Product, Category> categoryJoin = productJoin.join("categories");

            // Duyệt qua từng categoryType trong danh sách
            Predicate[] predicates = new Predicate[categoryTypes.size()];
            for (int i = 0; i < categoryTypes.size(); i++) {
                predicates[i] = criteriaBuilder.equal(categoryJoin.get("categoryType"), categoryTypes.get(i));
            }

            // Sử dụng `criteriaBuilder.and` để yêu cầu tất cả danh mục đều phải khớp
            return criteriaBuilder.and(predicates);
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