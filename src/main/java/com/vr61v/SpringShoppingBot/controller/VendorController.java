package com.vr61v.SpringShoppingBot.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

public interface VendorController {

    SendMessage createVendor(String chatId, Map<String, String> vendorFields);

    SendMessage updateVendor(String chatId, Map<String, String> vendorFields);

    SendMessage searchVendor(String chatId, String name);

    SendMessage deleteVendor(String chatId, String vendorId);
    
}
