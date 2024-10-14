package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.model.Category;
import com.example.bidMarket.repository.CategoryRepository;
import com.example.bidMarket.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category findByCategoryType(CategoryType categoryType) throws Exception {
        return categoryRepository.findByCategoryType(categoryType)
                .orElseThrow(() -> new Exception("Category not found for type: " + categoryType));
    }

    @Override
    public Category createCategory(CategoryType categoryType) {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryType(categoryType);
        if (categoryOptional.isPresent()){
            log.info(categoryType + " is exited");
            return categoryOptional.get();
        } else {
            Category category = Category.builder()
                    .categoryType(categoryType)
                    .build();
            return categoryRepository.save(category);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(UUID categoryId, Category updatedCategory) throws Exception {
        return null;
    }

    @Override
    public void deleteCategory(UUID categoryId) throws Exception {

    }
}
