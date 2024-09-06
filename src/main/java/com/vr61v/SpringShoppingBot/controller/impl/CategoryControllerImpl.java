package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.CategoryController;
import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;
import com.vr61v.SpringShoppingBot.service.CategoryService;
import com.vr61v.SpringShoppingBot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {

    private final ProductService productService;

    private final CategoryService categoryService;

    public SendMessage createCategory(String chatId, Map<String, String> categoryField) {
        String name = categoryField.get("name");
        boolean isForOverEighteen = Boolean.parseBoolean(categoryField.get("is_for_over_eighteen"));
        CreateCategoryRequest request = new CreateCategoryRequest(name, isForOverEighteen);

        Category response;
        try {
            response = categoryService.saveCategory(request);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Category creation failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text(String.format("Category created: %s", response))
                .build();
    }

    public SendMessage updateCategory(String chatId, Map<String, String> categoryField) {
        UUID id = UUID.fromString(categoryField.get("id"));
        String name = categoryField.get("name");
        boolean isForOverEighteen = Boolean.parseBoolean(categoryField.get("is_for_over_eighteen"));
        UpdateCategoryRequest request = new UpdateCategoryRequest(name, isForOverEighteen);

        Category response;
        try {
            response = categoryService.updateCategory(id, request);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Category update failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text(String.format("Category updated: %s", response))
                .build();
    }

    public SendMessage searchCategory(String chatId, String name) {
        Category response = categoryService.getCategoryByName(name);
        if (response == null) {
            return SendMessage.builder()
                    .chatId(chatId).text("Category not found")
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId).text("Category found: " + response)
                .build();
    }

    public SendMessage deleteCategory(String chatId, String categoryId) {
        if (productService.getProductsByCategoryId(UUID.fromString(categoryId)) != null) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Category with id: %s is not empty", categoryId))
                    .build();
        }

        try {
            categoryService.deleteCategoryById(UUID.fromString(categoryId));
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Category delete failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text(String.format("Category with id: %s deleted", categoryId))
                .build();
    }

}
