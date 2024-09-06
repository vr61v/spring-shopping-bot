package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;

import java.util.List;
import java.util.UUID;

public interface VendorService {

    Vendor saveVendor(CreateVendorRequest request);

    Vendor getVendorByName(String name);

    Vendor updateVendor(UUID id, UpdateVendorRequest request);

    void deleteVendorById(UUID id);
    
}
