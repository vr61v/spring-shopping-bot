package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.CategoryController;
import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.category.UpdateCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminCategoryRequestInterceptor {

    private final ProductController productController;

    private final CategoryController categoryController;

    public SendMessage createCategory(String chatId, Map<String, String> categoryField) {
        String name = categoryField.get("name");
        Boolean isForOverEighteen = Boolean.valueOf(categoryField.get("is_for_over_eighteen"));
        CreateCategoryRequest request = new CreateCategoryRequest(name, isForOverEighteen);

        ResponseEntity<Category> response = categoryController.createCategory(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Category created: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage updateCategory(String chatId, Map<String, String> categoryField) {
        UUID id = UUID.fromString(categoryField.get("id"));
        String name = categoryField.get("name");
        Boolean isForOverEighteen = Boolean.valueOf(categoryField.get("is_for_over_eighteen"));
        UpdateCategoryRequest request = new UpdateCategoryRequest(name, isForOverEighteen);

        ResponseEntity<Category> response = categoryController.updateCategory(id, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Category updated: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage searchCategory(String chatId, String name) {
        ResponseEntity<Category> response = categoryController.getCategoryByName(name);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return SendMessage.builder().chatId(chatId).text("Category found: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Category with name: " + name + " not found").build();
    }

    public SendMessage deleteCategory(String chatId, String categoryId) {
        if (productController.getProductsByCategoryId(UUID.fromString(categoryId)).getBody() != null) {
            return SendMessage.builder().chatId(chatId).text("Category with id:" + categoryId + " is not empty").build();
        }

        ResponseEntity<Category> response = categoryController.deleteCategory(UUID.fromString(categoryId));
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Category with id: " + categoryId + "deleted").build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

}
