package com.example.bidMarket.SearchService;
import com.example.bidMarket.Enum.Role;
import com.example.bidMarket.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserSpecification {

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) ->
                role == null ? null : criteriaBuilder.equal(root.get("role"), role);
    }

    public static Specification<User> isBanned(Boolean isBanned) {
        return (root, query, criteriaBuilder) ->
                isBanned == null ? null : criteriaBuilder.equal(root.get("isBanned"), isBanned);
    }

    public static Specification<User> isVerified(Boolean isVerified) {
        return (root, query, criteriaBuilder) ->
                isVerified == null ? null : criteriaBuilder.equal(root.get("isVerified"), isVerified);
    }

    public static Specification<User> hasId(UUID id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }
}
