package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.product.UpdateProductRequest;
import com.vr61v.SpringShoppingBot.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductControllerTelegramBot implements ProductController {

    private final ProductService productService;

    @Override
    public ResponseEntity<Product> createProduct(CreateProductRequest request) {
        Product product = productService.saveProduct(request);
        log.info("Create product: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Override
    public ResponseEntity<Product> getProductById(UUID id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            log.info("Product with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get product by id {}: {}", id, product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(List<UUID> ids) {
        List<Product> products = productService.getProducts(ids);
        if (products.isEmpty()) {
            log.info("Products with ids {} not found", ids);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get products by ids {}", ids);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<List<Product>> getProductsByName(String name) {
        List<Product> product = productService.getProductsByName(name);
        if (product.isEmpty()) {
            log.info("Products with name {} not found", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get products by name {}", name);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Override
    public ResponseEntity<List<Product>> getProductByDescription(String description) {
        List<Product> products = productService.getProductsByDescription(description);
        if (products.isEmpty()) {
            log.info("Products with description \"{}\" not found", description);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get products by description \"{}\"", description);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<List<Product>> getProductsByCategoryId(UUID categoryId) {
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        if (products.isEmpty()) {
            log.info("Products with category id {} not found", categoryId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get products by category id {}", categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<List<Product>> getProductsByVendorId(UUID vendorId) {
        List<Product> products = productService.getProductsByVendorId(vendorId);
        if (products.isEmpty()) {
            log.info("Products with vendor id {} not found", vendorId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get products by vendor id {}", vendorId);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<List<Product>> getProductPage(int from, int size) {
        List<Product> products = productService.getProductPage(from, size);
        if (products.isEmpty()) {
            log.info("Products not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get all products");
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Override
    public ResponseEntity<Long> getProductsCount() {
        Long count = productService.getProductsCount();
        log.info("Get product count {}", count);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @Override
    public ResponseEntity<Product> updateProduct(UUID id, UpdateProductRequest request) {
        Product product;
        try {
            product = productService.updateProduct(id, request);
        } catch (Exception e) {
            log.error("Update product with id {} failed: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Update product with id {}: {}", id, product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Override
    public ResponseEntity<Product> deleteProduct(UUID id) {
        try {
            productService.deleteProductById(id);
        } catch (Exception e) {
            log.error("Delete product with id {} failed: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Delete product with id {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
