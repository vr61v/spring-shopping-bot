package com.vr61v.SpringShoppingBot.service.impl;

import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;
import com.vr61v.SpringShoppingBot.repository.VendorRepository;
import com.vr61v.SpringShoppingBot.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor saveVendor(CreateVendorRequest request) {
        Vendor vendor = Vendor.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .build();
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor getVendorByName(String name) {
        return vendorRepository.findByName(name).orElse(null);
    }

    @Override
    public Vendor updateVendor(UUID id, UpdateVendorRequest request) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        if (request.name() != null) vendor.setName(request.name());
        if (request.description() != null) vendor.setDescription(request.description());

        return vendorRepository.save(vendor);
    }

    @Override
    public void deleteVendorById(UUID id) {
        vendorRepository.deleteById(id);
    }

}
