package com.example.bidMarket.SearchService;

import com.example.bidMarket.model.Order;
import com.example.bidMarket.Enum.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderSpecification {

    public static Specification<Order> hasBidderId(UUID userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Order> hasBidderEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(root.get("user").get("email"), email);
    }

    public static Specification<Order> hasSellerId(UUID sellerId) {
        return (root, query, criteriaBuilder) ->
                sellerId == null ? null : criteriaBuilder.equal(root.get("auction").get("product").get("seller").get("id"), sellerId);
    }

    public static Specification<Order> hasAuctionId(UUID auctionId) {
        return (root, query, criteriaBuilder) ->
                auctionId == null ? null : criteriaBuilder.equal(root.get("auction").get("id"), auctionId);
    }

    public static Specification<Order> hasAuctionTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.equal(root.get("auction").get("title"), title);
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Order> hasTotalAmountGreaterThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.greaterThan(root.get("totalAmount"), amount);
    }

    public static Specification<Order> hasTotalAmountLessThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.lessThan(root.get("totalAmount"), amount);
    }

    public static Specification<Order> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                date == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), date);
    }

    public static Specification<Order> createdBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                date == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), date);
    }

    public static Specification<Order> paymentDueAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                date == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("paymentDueDate"), date);
    }

    public static Specification<Order> paymentDueBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                date == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("paymentDueDate"), date);
    }
}
