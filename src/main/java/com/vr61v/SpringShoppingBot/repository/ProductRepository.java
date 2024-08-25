package com.vr61v.SpringShoppingBot.repository;

import com.vr61v.SpringShoppingBot.document.Category;
import com.vr61v.SpringShoppingBot.document.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, UUID> {

    List<Product> findByDescriptionContainingIgnoreCase(String description);

    List<Product> findByCategory(UUID id);

    List<Product> findByVendor(UUID id);
}
