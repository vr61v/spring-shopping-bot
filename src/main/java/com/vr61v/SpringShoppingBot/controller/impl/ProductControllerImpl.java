package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.entity.Category;
import com.vr61v.SpringShoppingBot.entity.Product;
import com.vr61v.SpringShoppingBot.entity.Vendor;
import com.vr61v.SpringShoppingBot.entity.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.entity.request.product.UpdateProductRequest;
import com.vr61v.SpringShoppingBot.service.CategoryService;
import com.vr61v.SpringShoppingBot.service.ProductService;
import com.vr61v.SpringShoppingBot.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    private final VendorService vendorService;

    private Product createProductByFields(Map<String, String> productFields) {
        String name = productFields.get("name");
        float price = Float.parseFloat(productFields.getOrDefault("price", "0"));
        String description = productFields.get("description");
        Category category = categoryService.getCategoryByName(productFields.get("category"));
        Vendor vendor = vendorService.getVendorByName(productFields.get("vendor"));

        return Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .categoryId(category.getId())
                .vendorId(vendor.getId())
                .build();
    }

    public SendMessage createProduct(String chatId, Map<String, String> productFields) {
        Product product = createProductByFields(productFields);
        if (product.getCategoryId() == null) return SendMessage.builder().chatId(chatId).text("Category not found").build();
        if (product.getVendorId() == null) return SendMessage.builder().chatId(chatId).text("Vendor not found").build();

        CreateProductRequest request = new CreateProductRequest(
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getCategoryId(),
                product.getVendorId()
        );

        Product response;
        try {
            response = productService.saveProduct(request);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Product creation failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text("Product created: " + response)
                .build();
    }

    public SendMessage updateProduct(String chatId, Map<String, String> productFields) {
        UUID id = UUID.fromString(productFields.get("id"));
        Product product = createProductByFields(productFields);
        if (product.getCategoryId() == null) return SendMessage.builder().chatId(chatId).text("Category not found").build();
        if (product.getVendorId() == null) return SendMessage.builder().chatId(chatId).text("Vendor not found").build();

        UpdateProductRequest request = new UpdateProductRequest(
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getCategoryId(),
                product.getVendorId()
        );

        Product response;
        try{
            response = productService.updateProduct(id, request);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Product update failed: %s", e.getMessage()))
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId).text(String.format("Product updated: %s", response))
                .build();
    }

    public SendMessage searchProducts(String chatId, String name) {
        List<Product> response = productService.getProductsByName(name);
        if (!response.isEmpty()) {
            StringBuilder text = new StringBuilder("Products found: \n");
            for (int i = 0; i < response.size(); ++i) {
                text.append(i + 1).append(". ")
                        .append(response.get(i).toString()).append("\n");
            }
            return SendMessage.builder()
                    .chatId(chatId).text(text.toString())
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId).text("Products not found")
                .build();
    }

    public SendMessage deleteProduct(String chatId, String productId) {
        try {
            productService.deleteProductById(UUID.fromString(productId));
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Product delete failed: %s", e.getMessage()))
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId).text(String.format("Product with id: %s deleted", productId))
                .build();
    }

}
