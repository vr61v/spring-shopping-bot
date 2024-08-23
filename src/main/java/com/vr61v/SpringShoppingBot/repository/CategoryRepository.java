package com.vr61v.SpringShoppingBot.repository;

import com.vr61v.SpringShoppingBot.document.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends ElasticsearchRepository<Category, UUID> {

    Optional<Category> findByName(String name);

}
