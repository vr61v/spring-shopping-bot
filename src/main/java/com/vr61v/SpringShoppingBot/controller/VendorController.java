package com.vr61v.SpringShoppingBot.controller;

import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface VendorController {

    ResponseEntity<Vendor> createVendor(CreateVendorRequest request);

    ResponseEntity<Vendor> getVendorById(UUID id);

    ResponseEntity<Vendor> getVendorByName(String name);

    ResponseEntity<List<Vendor>> getAllVendors();

    ResponseEntity<Vendor> updateVendor(UUID id, UpdateVendorRequest request);

    ResponseEntity<Vendor> deleteVendor(UUID id);
    
}
