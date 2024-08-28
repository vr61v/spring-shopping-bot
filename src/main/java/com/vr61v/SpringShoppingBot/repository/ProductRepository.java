package com.vr61v.SpringShoppingBot.repository;

import com.vr61v.SpringShoppingBot.document.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, UUID> {

    List<Product> findByName(String name);

    List<Product> findByDescriptionContainingIgnoreCase(String description);

    List<Product> findByCategoryId(UUID id);

    List<Product> findByVendorId(UUID id);

}
