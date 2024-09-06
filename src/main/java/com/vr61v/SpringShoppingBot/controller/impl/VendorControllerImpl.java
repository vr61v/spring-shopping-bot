package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.VendorController;
import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;
import com.vr61v.SpringShoppingBot.service.ProductService;
import com.vr61v.SpringShoppingBot.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VendorControllerImpl implements VendorController {

    private final VendorService vendorService;

    private final ProductService productService;

    public SendMessage createVendor(String chatId, Map<String, String> vendorFields) {
        String name = vendorFields.get("name");
        String description = vendorFields.get("description");
        CreateVendorRequest request = new CreateVendorRequest(name, description);

        Vendor response;
        try {
            response = vendorService.saveVendor(request);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Vendor creation failed: %s", e.getMessage()))
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId).text(String.format("Vendor created: %s", response))
                .build();
    }

    public SendMessage updateVendor(String chatId, Map<String, String> vendorFields) {
        UUID id = UUID.fromString(vendorFields.get("id"));
        String name = vendorFields.get("name");
        String description = vendorFields.get("description");
        UpdateVendorRequest request = new UpdateVendorRequest(name, description);

        Vendor response;
        try {
            response = vendorService.updateVendor(id, request);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Vendor update failed: %s", e.getMessage()))
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId).text(String.format("Vendor updated: %s", response))
                .build();
    }

    public SendMessage searchVendor(String chatId, String name) {
        Vendor response = vendorService.getVendorByName(name);
        if (response == null) {
            return SendMessage.builder()
                    .chatId(chatId).text("Vendor not found").build();
        }
        return SendMessage.builder()
                .chatId(chatId).text("Vendor found: " + response).build();
    }

    public SendMessage deleteVendor(String chatId, String vendorId) {
        if (productService.getProductsByVendorId(UUID.fromString(vendorId)) != null) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Vendor with id: %s is not empty", vendorId))
                    .build();
        }

        try {
            vendorService.deleteVendorById(UUID.fromString(vendorId));
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Vendor delete failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text(String.format("Vendor with id: %s deleted", vendorId))
                .build();
    }

}
