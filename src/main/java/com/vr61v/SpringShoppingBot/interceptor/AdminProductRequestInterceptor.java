package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.CategoryController;
import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.controller.VendorController;
import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.product.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminProductRequestInterceptor {

    private final ProductController productController;

    private final CategoryController categoryController;

    private final VendorController vendorController;

    public SendMessage createProduct(String chatId, Map<String, String> productFields) {
        String name = productFields.get("name");
        String price = productFields.get("price");
        String count = productFields.get("count");
        String description = productFields.get("description");

        String categoryName = productFields.get("category");
        ResponseEntity<Category> categoryResponse = categoryController.getCategoryByName(categoryName);
        if (categoryResponse.getBody() == null)
            return SendMessage.builder().chatId(chatId).text("Invalid category").build();
        UUID categoryId = categoryResponse.getBody().getId();

        String vendorName = productFields.get("vendor");
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

    public SendMessage updateProduct(String chatId, Map<String, String> productFields) {
        UUID id = UUID.fromString(productFields.get("id"));
        String name = productFields.get("name");
        Float price = Float.valueOf(productFields.getOrDefault("price", "0"));
        Integer count = Integer.valueOf(productFields.getOrDefault("count", "0"));
        String description = productFields.get("description");

        String categoryName = productFields.get("category");
        ResponseEntity<Category> categoryResponse = categoryController.getCategoryByName(categoryName);
        if (!categoryResponse.getStatusCode().is2xxSuccessful())
            return SendMessage.builder().chatId(chatId).text("Invalid category").build();

        String vendorName = productFields.get("vendor");
        ResponseEntity<Vendor> vendorResponse = vendorController.getVendorByName(vendorName);
        if (vendorResponse.getStatusCode().is2xxSuccessful())
            return SendMessage.builder().chatId(chatId).text("Invalid vendor").build();

        UpdateProductRequest request = new UpdateProductRequest(
                name,
                price,
                count,
                description,
                categoryResponse.getBody() != null ? categoryResponse.getBody().getId() : null,
                vendorResponse.getBody() != null ? vendorResponse.getBody().getId() : null
        );
        ResponseEntity<Product> response = productController.updateProduct(id, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Product updated: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage searchProducts(String chatId, String name) {
        ResponseEntity<List<Product>> response = productController.getProductsByName(name);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Product> products = response.getBody();
            StringBuilder text = new StringBuilder("Products found: \n");
            for (int i = 0; i < products.size(); ++i) {
                text.append(i + 1).append(". ").append(products.get(i).toString()).append("\n");
            }
            return SendMessage.builder().chatId(chatId).text(text.toString()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Product not found").build();
    }

    public SendMessage deleteProduct(String chatId, String productId) {
        ResponseEntity<Product> response = productController.deleteProduct(UUID.fromString(productId));
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Product with id:" + productId + "deleted").build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

}
