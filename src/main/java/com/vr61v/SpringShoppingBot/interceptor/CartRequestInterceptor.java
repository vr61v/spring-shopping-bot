package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.document.Cart;
import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.service.CartService;
import com.vr61v.SpringShoppingBot.service.ProductService;
import com.vr61v.SpringShoppingBot.ui.CartMenuUI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartRequestInterceptor {

    private final CartService cartService;

    private final ProductService productService;

    private SendMessage openCartMenu(String chatId, String username) {
        StringBuilder text = new StringBuilder();
        float total = 0;
        Cart response = cartService.getCartByUsername(username);

        if (response != null) {
            HashMap<UUID, Integer> productsCount = response.getProducts();
            List<Product> products = productService.getProducts(
                    productsCount
                            .keySet()
                            .stream()
                            .toList()
            );

            for (int i = 0; i < products.size(); ++i) {
                Product product = products.get(i);
                total += product.getPrice() * productsCount.get(product.getId());
                text.append(String.format("%d) %s %.2fр. (%dшт.)\n{id:`%s`}\n",
                        i + 1, product.getName(), product.getPrice(), productsCount.get(product.getId()), product.getId()));
            }
        }

        if (text.isEmpty()) text.append("Cart is empty");
        else text.append(String.format("\nTotal price in cart: %.2f", total));

        return CartMenuUI.getCartMenu(chatId, text.toString());
    }

    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();
        String username = callbackQuery.getFrom().getUserName();

        if (data.contains("OPEN_MENU")) {
            return openCartMenu(chatId, username);
        } else if (data.contains("ADD_TO")) {
            chatState.put(username, UserState.CART_WAITING_ADD_IN);
            return SendMessage.builder().chatId(chatId).text("Enter product id").build();
        } else if (data.contains("REMOVE_FROM")) {
            chatState.put(username, UserState.CART_WAITING_REMOVE_FROM);
            return SendMessage.builder().chatId(chatId).text("Enter product id").build();
        }

        return SendMessage.builder().text("Invalid request").build();
    }

}
