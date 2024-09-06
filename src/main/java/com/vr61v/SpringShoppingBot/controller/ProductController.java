package com.vr61v.SpringShoppingBot.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

public interface ProductController {

    SendMessage createProduct(String chatId, Map<String, String> productFields);

    SendMessage updateProduct(String chatId, Map<String, String> productFields);

    SendMessage searchProducts(String chatId, String name);

    SendMessage deleteProduct(String chatId, String productId);

}
