package com.vr61v.SpringShoppingBot.service;

import com.vr61v.SpringShoppingBot.document.Cart;
import com.vr61v.SpringShoppingBot.document.Product;

import java.util.UUID;

public interface CartService {

    Cart getCart(String username);

    Cart addProductToCart(String username, UUID product);

    Cart removeProductFromCart(String username, UUID product);

}
