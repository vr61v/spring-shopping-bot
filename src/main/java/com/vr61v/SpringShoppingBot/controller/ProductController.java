package com.vr61v.SpringShoppingBot.controller;

import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.product.UpdateProductRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProductController {

    ResponseEntity<Product> createProduct(CreateProductRequest request);

    ResponseEntity<Product> getProductById(UUID id);

    ResponseEntity<List<Product>> getProducts(List<UUID> ids);

    ResponseEntity<List<Product>> getProductsByName(String name);

    ResponseEntity<List<Product>> getProductByDescription(String description);

    ResponseEntity<List<Product>> getProductsByCategoryId(UUID categoryId);

    ResponseEntity<List<Product>> getProductsByVendorId(UUID vendorId);

    ResponseEntity<List<Product>> getAllProducts();

    ResponseEntity<Product> updateProduct(UUID id, UpdateProductRequest request);

    ResponseEntity<Product> deleteProduct(UUID id);

}
