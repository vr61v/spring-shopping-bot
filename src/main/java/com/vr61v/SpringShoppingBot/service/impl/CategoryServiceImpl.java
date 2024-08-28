package com.vr61v.SpringShoppingBot.service.impl;

import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;
import com.vr61v.SpringShoppingBot.repository.CategoryRepository;
import com.vr61v.SpringShoppingBot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .isForOverEighteen(request.isForOverEighteen())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(UUID id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        if (request.name() != null) category.setName(request.name());
        if (request.isForOverEighteen() != null) category.setIsForOverEighteen(request.isForOverEighteen());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }

}
