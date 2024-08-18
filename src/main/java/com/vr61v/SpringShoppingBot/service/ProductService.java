package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.document.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    Optional<Product> getProductById(UUID id);

    List<Product> getAllProducts();

    void deleteProductById(UUID id);

}
