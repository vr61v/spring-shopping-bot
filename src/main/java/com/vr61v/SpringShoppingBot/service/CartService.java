package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.document.Cart;

import java.util.UUID;

public interface CartService {

    Cart getCartByUsername(String username);

    void addProductInCart(String username, UUID product);

    void removeProductFromCart(String username, UUID product);

}
