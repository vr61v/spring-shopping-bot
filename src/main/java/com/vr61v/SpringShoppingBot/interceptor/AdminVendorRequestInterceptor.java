package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.controller.VendorController;
import com.vr61v.SpringShoppingBot.document.Vendor;
import com.vr61v.SpringShoppingBot.document.request.vendor.CreateVendorRequest;
import com.vr61v.SpringShoppingBot.document.request.vendor.UpdateVendorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminVendorRequestInterceptor {

    private final VendorController vendorController;

    private final ProductController productController;

    public SendMessage createVendor(String chatId, Map<String, String> vendorFields) {
        String name = vendorFields.get("name");
        String description = vendorFields.get("description");
        CreateVendorRequest request = new CreateVendorRequest(name, description);

        ResponseEntity<Vendor> response = vendorController.createVendor(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Vendor created: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage updateVendor(String chatId, Map<String, String> vendorFields) {
        UUID id = UUID.fromString(vendorFields.get("id"));
        String name = vendorFields.get("name");
        String description = vendorFields.get("description");
        UpdateVendorRequest request = new UpdateVendorRequest(name, description);

        ResponseEntity<Vendor> response = vendorController.updateVendor(id, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Vendor updated: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    public SendMessage searchVendor(String chatId, String name) {
        ResponseEntity<Vendor> response = vendorController.getVendorByName(name);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return SendMessage.builder().chatId(chatId).text("Vendor found: " + response.getBody().getName()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Vendor with name: " + name + " not found").build();
    }

    public SendMessage deleteVendor(String chatId, String vendorId) {
        if (productController.getProductsByVendorId(UUID.fromString(vendorId)).getBody() != null) {
            return SendMessage.builder().chatId(chatId).text("Vendor with id:" + vendorId + " is not empty").build();
        }

        ResponseEntity<Vendor> response = vendorController.deleteVendor(UUID.fromString(vendorId));
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Vendor with id:" + vendorId + "deleted").build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

}
