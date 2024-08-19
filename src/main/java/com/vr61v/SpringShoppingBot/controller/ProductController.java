package com.vr61v.SpringShoppingBot.controller;


import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.product.UpdateProductRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ProductController {

    ResponseEntity<?> createProduct(CreateProductRequest request);

    ResponseEntity<?> getProductById(UUID id);

    ResponseEntity<?> getProductByDescription(String description);

    ResponseEntity<?> getAllProducts();

    ResponseEntity<?> updateProduct(UUID id, UpdateProductRequest request);

    ResponseEntity<?> deleteProduct(UUID id);

}
