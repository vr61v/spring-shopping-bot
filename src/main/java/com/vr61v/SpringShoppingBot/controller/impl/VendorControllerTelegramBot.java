package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.VendorController;
import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;
import com.vr61v.SpringShoppingBot.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendorControllerTelegramBot implements VendorController {

    private final VendorService vendorService;

    @Override
    public ResponseEntity<?> createVendor(CreateVendorRequest request) {
        Vendor vendor = vendorService.saveVendor(request);
        log.info("Create vendor: {}", vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendor);
    }

    @Override
    public ResponseEntity<?> getVendorById(UUID id) {
        Vendor vendor = vendorService.getVendorById(id);
        if (vendor == null) {
            log.info("Get vendor with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get vendor by id {}: {}", id, vendor);
        return ResponseEntity.status(HttpStatus.OK).body(vendor);
    }

    @Override
    public ResponseEntity<?> getVendorByName(String name) {
        Vendor vendor = vendorService.getVendorByName(name);
        if (vendor == null) {
            log.info("Get vendor with name {} not found", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get vendor by name {}: {}", name, vendor);
        return null;
    }

    @Override
    public ResponseEntity<?> getAllVendors() {
        List<Vendor> vendors = vendorService.getAllVendors();
        if (vendors.isEmpty()) {
            log.info("Vendors not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Get all vendors: {}", vendors);
        return ResponseEntity.status(HttpStatus.OK).body(vendors);
    }

    @Override
    public ResponseEntity<?> updateVendor(UUID id, UpdateVendorRequest request) {
        Vendor vendor;
        try {
            vendor = vendorService.updateVendor(id, request);
        } catch (Exception e) {
            log.error("Update vendor with id {} failed: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Update vendor with id {}: {}", id, vendor);
        return ResponseEntity.status(HttpStatus.OK).body(vendor);
    }

    @Override
    public ResponseEntity<?> deleteVendor(UUID id) {
        try {
            vendorService.deleteVendorById(id);
        } catch (Exception e) {
            log.error("Delete vendor with id {} failed: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Delete vendor with id {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
