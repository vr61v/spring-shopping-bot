package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.CategoryController;
import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;
import com.vr61v.SpringShoppingBot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryControllerTelegramBot implements CategoryController {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest request) {
        Category category = categoryService.saveCategory(request);
        log.info("Create category: {}", category);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Override
    public ResponseEntity<?> getCategoryById(UUID id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            log.info("Category with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get product by id {}: {}", id, category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @Override
    public ResponseEntity<?> getCategoryByName(String name) {
        Category category = categoryService.getCategoryByName(name);
        if (category == null) {
            log.info("Category with name {} not found", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get product by name {}: {}", name, category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @Override
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            log.info("Categories not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get all categories: {}", categories);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @Override
    public ResponseEntity<?> updateCategory(UUID id, UpdateCategoryRequest request) {
        Category category;
        try {
            category = categoryService.updateCategory(id, request);
        } catch (Exception e) {
            log.error("Update category with id {} failed: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Update category with id {}: {}", id, category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @Override
    public ResponseEntity<?> deleteCategory(UUID id) {
        try {
            categoryService.deleteCategoryById(id);
        } catch (Exception e) {
            log.error("Delete category with id {} failed: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Delete category with id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
