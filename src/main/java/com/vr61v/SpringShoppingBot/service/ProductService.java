package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.entity.Product;
import com.vr61v.SpringShoppingBot.entity.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.entity.request.product.UpdateProductRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product saveProduct(CreateProductRequest request);

    Product getProductById(UUID id);

    List<Product> getProducts(List<UUID> ids);

    List<Product> getProductsByName(String name);

    List<Product> searchProductsByField(String field, String value, int from, int size);

    List<Product> getProductsByCategoryId(UUID categoryId);

    List<Product> getProductsByVendorId(UUID vendorId);

    List<Product> getProductPage(int from, int size);

    long getProductsCount();

    Product updateProduct(UUID id, UpdateProductRequest request);

    void deleteProductById(UUID id);

}
