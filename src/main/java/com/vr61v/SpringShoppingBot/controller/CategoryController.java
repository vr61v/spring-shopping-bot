package com.vr61v.SpringShoppingBot.controller;

import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CategoryController {

    ResponseEntity<Category> createCategory(CreateCategoryRequest request);

    ResponseEntity<Category> getCategoryById(UUID id);

    ResponseEntity<Category> getCategoryByName(String name);

    ResponseEntity<List<Category>> getAllCategories();

    ResponseEntity<Category> updateCategory(UUID id, UpdateCategoryRequest request);

    ResponseEntity<Category> deleteCategory(UUID id);


}
