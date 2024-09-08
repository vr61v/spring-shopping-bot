package com.vr61v.SpringShoppingBot.repository;

import com.vr61v.SpringShoppingBot.document.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, UUID> {

    List<Product> findAllByIdIn(List<UUID> ids);

    List<Product> findAllByName(String name);

    List<Product> findByCategoryId(UUID id);

    List<Product> findByVendorId(UUID id);

    @Query("{\"match\":{\"?0\":{\"query\":\"?1\"}}}")
    List<Product> findProductsByField(String field, String value, Pageable pageable);


}
