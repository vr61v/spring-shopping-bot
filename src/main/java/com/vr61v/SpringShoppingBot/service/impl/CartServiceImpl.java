package com.vr61v.SpringShoppingBot.service.impl;

import com.vr61v.SpringShoppingBot.document.Cart;
import com.vr61v.SpringShoppingBot.repository.CartRepository;
import com.vr61v.SpringShoppingBot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Cart getCartByUsername(String username) {
        Cart cart = cartRepository.findById(username).orElse(null);
        if (cart == null) {
            cart = Cart.builder().username(username).products(new HashMap<>()).build();
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public void addProductInCart(String username, UUID product) {
        Cart cart = getCartByUsername(username);
        cart.getProducts().put(product, cart.getProducts().getOrDefault(product, 0) + 1);

        cartRepository.save(cart);
    }

    @Override
    public void removeProductFromCart(String username, UUID product) {
        Cart cart = getCartByUsername(username);
        cart.getProducts().put(product, cart.getProducts().getOrDefault(product, 0) - 1);
        if (cart.getProducts().get(product) <= 0) cart.getProducts().remove(product);

        cartRepository.save(cart);
    }
}
