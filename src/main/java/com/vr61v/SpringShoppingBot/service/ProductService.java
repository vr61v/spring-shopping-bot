package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.product.UpdateProductRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product saveProduct(CreateProductRequest request);

    Product getProductById(UUID id);

    List<Product> getProducts(List<UUID> ids);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByDescription(String description);

    List<Product> getProductsByCategoryId(UUID categoryId);

    List<Product> getProductsByVendorId(UUID vendorId);

    List<Product> getAllProducts();

    Product updateProduct(UUID id, UpdateProductRequest request);

    void deleteProductById(UUID id);

}
