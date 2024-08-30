package com.vr61v.SpringShoppingBot.controller;

import com.vr61v.SpringShoppingBot.document.Cart;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CartController {

    ResponseEntity<Cart> getCart(String username);

    ResponseEntity<Cart> addProductToCart(String username, UUID product);

    ResponseEntity<Cart> removeProductFromCart(String username, UUID product);

}
