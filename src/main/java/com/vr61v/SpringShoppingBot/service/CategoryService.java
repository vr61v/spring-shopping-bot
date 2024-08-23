package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    Category saveCategory(CreateCategoryRequest request);

    Category getCategoryById(UUID id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category updateCategory(UUID id, UpdateCategoryRequest request);

    void deleteCategoryById(UUID id);

}
