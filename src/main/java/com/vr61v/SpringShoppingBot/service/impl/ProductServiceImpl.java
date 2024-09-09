package com.vr61v.SpringShoppingBot.service.impl;

import com.vr61v.SpringShoppingBot.entity.Product;
import com.vr61v.SpringShoppingBot.entity.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.entity.request.product.UpdateProductRequest;
import com.vr61v.SpringShoppingBot.repository.ProductRepository;
import com.vr61v.SpringShoppingBot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

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
    public List<Product> getProducts(List<UUID> ids) {
        return productRepository.findAllByIdIn(ids);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findAllByName(name);
    }

    @Override
    public List<Product> searchProductsByField(String field, String value, int from, int size) {
        return productRepository.findProductsByField(field, value, PageRequest.of(from, size));
    }

    @Override
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsByVendorId(UUID vendorId) {
        return productRepository.findByVendorId(vendorId);
    }

    @Override
    public List<Product> getProductPage(int from, int size) {
        Page<Product> pageProducts = productRepository.findAll(PageRequest.of(from, size));
        return pageProducts.getContent();
    }

    @Override
    public long getProductsCount() {
        return productRepository.count();
    }

    @Override
    public Product updateProduct(UUID id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        if (request.name() != null) product.setName(request.name());
        if (request.price() != null) product.setPrice(request.price());
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
