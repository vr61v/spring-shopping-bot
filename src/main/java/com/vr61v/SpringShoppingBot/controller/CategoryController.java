package com.vr61v.SpringShoppingBot.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

public interface CategoryController {

    SendMessage createCategory(String chatId, Map<String, String> categoryFields);

    SendMessage updateCategory(String chatId, Map<String, String> categoryFields);

    SendMessage searchCategory(String chatId, String name);

    SendMessage deleteCategory(String chatId, String categoryId);

}
