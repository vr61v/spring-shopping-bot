package com.vr61v.SpringShoppingBot.repository;

import com.vr61v.SpringShoppingBot.document.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, UUID> {

    Optional<Product> findByName(String name);

}
