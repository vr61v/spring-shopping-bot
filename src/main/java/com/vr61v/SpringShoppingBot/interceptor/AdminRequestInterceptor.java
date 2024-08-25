package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.CategoryController;
import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.controller.VendorController;
import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.category.CreateCategoryRequest;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.ui.AdminMenuUI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AdminRequestInterceptor {

    private final ProductController productController;

    private final CategoryController categoryController;

    private final VendorController vendorController;

    private Map<String, String> parseStringToField(String entity) {
        List<String> lines = List.of(entity.split("\n"));
        Map<String, String> map = new HashMap<>();
        for (String line : lines) {
            String[] keyValue = line.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();
        if (data.contains("ADMIN_OPEN_MENU")){
            return openAdminMenu(chatId);
        } else if (data.contains("PRODUCT_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_CREATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter new product data").build();
        } else if (data.contains("PRODUCT_DELETE")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_DELETE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter product's id for delete").build();
        } else if (data.contains("CATEGORY_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.CATEGORY_WAITING_CREATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter new category data").build();
        }  else if (data.contains("CATEGORY_DELETE")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.CATEGORY_WAITING_DELETE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter category's id for delete").build();
        } else if (data.contains("VENDOR_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.VENDOR_WAITING_CREATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter new vendor data").build();
        } else if (data.contains("VENDOR_DELETE")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.VENDOR_WAITING_DELETE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter vendor's id for delete").build();
        }

        return SendMessage.builder().text("Invalid request").build();
    }

    private SendMessage openAdminMenu(String chatId) {
        return AdminMenuUI.getAdminUI(chatId);
    }

    public SendMessage createProduct(String chatId, String product) {
        Map<String, String> map = parseStringToField(product);
        String name = map.get("name");
        String price = map.get("price");
        String count = map.get("count");
        String description = map.get("description");

        String categoryName = map.get("category");
        ResponseEntity<Category> categoryResponse = categoryController.getCategoryByName(categoryName);
        if (categoryResponse.getBody() == null)
            return SendMessage.builder().chatId(chatId).text("Invalid category").build();
        UUID categoryId = categoryResponse.getBody().getId();

        String vendorName = map.get("vendor");
        ResponseEntity<Vendor> vendorResponse = vendorController.getVendorByName(vendorName);
        if (vendorResponse.getBody() == null)
            return SendMessage.builder().chatId(chatId).text("Invalid vendor").build();
        UUID vendorId = vendorResponse.getBody().getId();

        CreateProductRequest request = new CreateProductRequest(
                name,
                price != null ? Float.parseFloat(price) : 0,
                count != null ? Integer.parseInt(count) : 0,
                description,
                categoryId,
                vendorId
        );

        ResponseEntity<Product> response = productController.createProduct(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Product created: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage deleteProduct(String chatId, String productId) {
        ResponseEntity<Product> response = productController.deleteProduct(UUID.fromString(productId));
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Product with id:" + productId + "deleted").build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage createCategory(String chatId, String category) {
        Map<String, String> map = parseStringToField(category);
        String name = map.get("name");
        Boolean isForOverEighteen = Boolean.valueOf(map.get("is_for_over_eighteen"));
        CreateCategoryRequest request = new CreateCategoryRequest(name, isForOverEighteen);

        ResponseEntity<Category> response = categoryController.createCategory(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Category created: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage deleteCategory(String chatId, String categoryId) {
        if (productController.getProductsByCategoryId(UUID.fromString(categoryId)).getBody() != null) {
            return SendMessage.builder().chatId(chatId).text("Category with id:" + categoryId + " is not empty").build();
        }
        
        ResponseEntity<Category> response = categoryController.deleteCategory(UUID.fromString(categoryId)); 
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Category with id:" + categoryId + "deleted").build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage createVendor(String chatId, String vendor) {
        Map<String, String> map = parseStringToField(vendor);
        String name = map.get("name");
        String description = map.get("description");
        CreateVendorRequest request = new CreateVendorRequest(name, description);

        ResponseEntity<Vendor> response = vendorController.createVendor(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Vendor created: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage deleteVendor(String chatId, String vendorId) {
        if (productController.getProductsByVendorId(UUID.fromString(vendorId)).getBody() != null) {
            return SendMessage.builder().chatId(chatId).text("Vendor with id:" + vendorId + " is not empty").build();
        }

        ResponseEntity<Vendor> response = vendorController.deleteVendor(UUID.fromString(vendorId));
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Vendor with id:" + vendorId + "deleted").build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }
}
