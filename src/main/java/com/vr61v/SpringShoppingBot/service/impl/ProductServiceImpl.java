package com.vr61v.SpringShoppingBot.service.impl;

import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.document.request.product.UpdateProductRequest;
import com.vr61v.SpringShoppingBot.repository.ProductRepository;
import com.vr61v.SpringShoppingBot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product saveProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .price(request.price())
                .count(request.count())
                .description(request.description())
                .categoryId(request.categoryId())
                .vendorId(request.vendorId())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getProductsByDescription(String description) {
        return productRepository.findByDescriptionContainingIgnoreCase(description);
    }

    @Override
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        return productRepository.findByCategory(categoryId);
    }

    @Override
    public List<Product> getProductsByVendorId(UUID vendorId) {
        return productRepository.findByVendor(vendorId);
    }

    @Override
    public List<Product> getAllProducts() {
        Iterable<Product> products = productRepository.findAll();
        List<Product> productList = new ArrayList<>();
        for (Product product : products) productList.add(product);
        return productList;
    }

    @Override
    public Product updateProduct(UUID id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        if (request.name() != null) product.setName(request.name());
        if (request.price() != null) product.setPrice(request.price());
        if (request.count() != null) product.setCount(request.count());
        if (request.description() != null) product.setDescription(request.description());
        if (request.categoryId() != null) product.setCategoryId(request.categoryId());
        if (request.vendorId() != null) product.setVendorId(request.vendorId());

        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }
}
