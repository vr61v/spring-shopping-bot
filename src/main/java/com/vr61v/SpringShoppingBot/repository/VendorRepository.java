package com.vr61v.SpringShoppingBot.repository;

import com.vr61v.SpringShoppingBot.document.Vendor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository  extends ElasticsearchRepository<Vendor, UUID> {

    Optional<Vendor> findByName(String name);

}
