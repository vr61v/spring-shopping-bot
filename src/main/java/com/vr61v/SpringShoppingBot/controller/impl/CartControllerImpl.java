package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.CartController;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.service.CartService;
import com.vr61v.SpringShoppingBot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {

    private final CartService cartService;

    private final ProductService productService;

    public SendMessage addProductInCart(String chatId, String username, String productId) {
        Product productResponse = productService.getProductById(UUID.fromString(productId));
        if (productResponse == null) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Product with id %s not found", productId))
                    .build();
        }

        try {
            cartService.addProductInCart(username, UUID.fromString(productId));
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Add product in cart failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text(String.format("Product with id %s added in cart", productId))
                .build();
    }

    public SendMessage removeProductFromCart(String chatId, String username, String productId) {
        Product productResponse = productService.getProductById(UUID.fromString(productId));
        if (productResponse == null) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Product with id %s not found", productId))
                    .build();
        }

        try {
            cartService.removeProductFromCart(username, UUID.fromString(productId));
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId).text(String.format("Remove product from cart failed: %s", e.getMessage()))
                    .build();
        }

        return SendMessage.builder()
                .chatId(chatId).text(String.format("Product with id %s removed from cart", productId))
                .build();
    }

}
