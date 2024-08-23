package com.vr61v.SpringShoppingBot.controller;

import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryController {

    ResponseEntity<?> createCategory(CreateCategoryRequest request);

    ResponseEntity<?> getCategoryById(UUID id);

    ResponseEntity<?> getCategoryByName(String name);

    ResponseEntity<?> getAllCategories();

    ResponseEntity<?> updateCategory(UUID id, UpdateCategoryRequest request);

    ResponseEntity<?> deleteCategory(UUID id);


}
