package com.example.bidMarket.SearchService;


import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.model.Category;
import com.example.bidMarket.model.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasCategoryType(CategoryType categoryType) {
        return (root, query, criteriaBuilder) -> {
            if (categoryType == null) {
                return null;
            }
            Join<Product, Category> categoryJoin = root.join("categories");
            return criteriaBuilder.equal(categoryJoin.get("categoryType"), categoryType);
        };
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
