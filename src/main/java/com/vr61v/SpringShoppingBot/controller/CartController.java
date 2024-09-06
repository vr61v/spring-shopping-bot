package com.vr61v.SpringShoppingBot.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CartController {

    SendMessage addProductInCart(String chatId, String username, String productId);

    SendMessage removeProductFromCart(String chatId, String username, String productId);

}
