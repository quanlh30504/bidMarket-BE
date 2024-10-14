package com.example.bidMarket.service;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category findByCategoryType(CategoryType categoryType) throws Exception;

    Category createCategory(CategoryType categoryType);

    List<Category> getAllCategories();

    Category updateCategory(UUID categoryId, Category updatedCategory) throws Exception;

    void deleteCategory(UUID categoryId) throws Exception;
}
