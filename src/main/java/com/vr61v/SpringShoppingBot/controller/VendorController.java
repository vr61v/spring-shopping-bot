package com.vr61v.SpringShoppingBot.controller;

import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface VendorController {

    ResponseEntity<?> createVendor(CreateVendorRequest request);

    ResponseEntity<?> getVendorById(UUID id);

    ResponseEntity<?> getVendorByName(String name);

    ResponseEntity<?> getAllVendors();

    ResponseEntity<?> updateVendor(UUID id, UpdateVendorRequest request);

    ResponseEntity<?> deleteVendor(UUID id);
    
}
