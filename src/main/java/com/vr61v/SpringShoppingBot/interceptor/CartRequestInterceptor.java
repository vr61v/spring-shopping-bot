package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.CartController;
import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.document.Cart;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.ui.CartMenuUI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CartRequestInterceptor {

    private final CartController cartController;

    private final ProductController productController;

    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();
        String username = callbackQuery.getFrom().getUserName();

        if (data.contains("OPEN_MENU")) {
            return openCartMenu(chatId, username);
        } else if (data.contains("ADD_TO")) {
            chatState.put(username, UserState.CART_WAITING_ADD_TO);
            return SendMessage.builder().chatId(chatId).text("Enter product id").build();
        } else if (data.contains("REMOVE_FROM")) {
            chatState.put(username, UserState.CART_WAITING_REMOVE_FROM);
            return SendMessage.builder().chatId(chatId).text("Enter product id").build();
        }

        return SendMessage.builder().text("Invalid request").build();
    }

    public SendMessage openCartMenu(String chatId, String username) {
        StringBuilder text = new StringBuilder();
        float total = 0;
        ResponseEntity<Cart> responseCart = cartController.getCart(username);
        if (responseCart.getStatusCode().is2xxSuccessful() && responseCart.getBody() != null) {
            HashMap<UUID, Integer> productsCount = responseCart.getBody().getProducts();
            List<Product> products = productController
                    .getProducts(productsCount
                            .keySet()
                            .stream()
                            .toList())
                    .getBody();
            for (int i = 0; i < Objects.requireNonNull(products).size(); ++i) {
                Product product = products.get(i);
                total += product.getPrice() * productsCount.get(product.getId());
                text.append(String.format("%d) %s %.2fр. (%dшт.)\n{id:`%s`}\n",
                        i + 1, product.getName(), product.getPrice(), productsCount.get(product.getId()), product.getId()));
            }
        }
        if (text.isEmpty()) text.append("Cart is empty");
        else text.append(String.format("\nTotal price in cart: %.2f", total));
        return CartMenuUI.getCardUI(chatId, text.toString());
    }

    public SendMessage addProductToCart(String chatId, String username, String productId) {
        ResponseEntity<Product> productResponse = productController.getProductById(UUID.fromString(productId));
        if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
            ResponseEntity<Cart> cartResponse = cartController.addProductToCart(username, productResponse.getBody().getId());
            if (cartResponse.getStatusCode().is2xxSuccessful()) {
                return SendMessage.builder().chatId(chatId).text("Added product to Cart").build();
            }
            return SendMessage.builder().chatId(chatId).text("Invalid request").build();
        }

        return SendMessage.builder().chatId(chatId).text("Invalid productId").build();
    }

    public SendMessage removeProductToCart(String chatId, String username, String productId) {
        ResponseEntity<Product> productResponse = productController.getProductById(UUID.fromString(productId));
        if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
            ResponseEntity<Cart> cartResponse = cartController.removeProductFromCart(username, productResponse.getBody().getId());
            if (cartResponse.getStatusCode().is2xxSuccessful()) {
                return SendMessage.builder().chatId(chatId).text("Remove product from Cart").build();
            }
            return SendMessage.builder().chatId(chatId).text("Invalid request").build();
        }

        return SendMessage.builder().chatId(chatId).text("Invalid productId").build();
    }

}
