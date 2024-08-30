package com.vr61v.SpringShoppingBot.controller.impl;

import com.vr61v.SpringShoppingBot.controller.CartController;
import com.vr61v.SpringShoppingBot.document.Cart;
import com.vr61v.SpringShoppingBot.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartControllerTelegramBot implements CartController {

    private final CartService cartService;

    @Override
    public ResponseEntity<Cart> getCart(String username) {
        Cart cart = cartService.getCart(username);
        log.info("Get cart by username {}: {}", username, cart);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @Override
    public ResponseEntity<Cart> addProductToCart(String username, UUID product) {
        Cart cart = cartService.addProductToCart(username, product);
        log.info("Add product to cart {}", cart);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @Override
    public ResponseEntity<Cart> removeProductFromCart(String username, UUID product) {
        Cart cart = cartService.removeProductFromCart(username, product);
        log.info("Remove product from cart {}", cart);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
}
