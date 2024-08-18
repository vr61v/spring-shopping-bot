package com.vr61v.SpringShoppingBot.service.impl;

import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.repository.ProductRepository;
import com.vr61v.SpringShoppingBot.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of((Product) productRepository.findAll());
    }

    @Override
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }
}
