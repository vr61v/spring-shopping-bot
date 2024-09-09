package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.entity.Product;
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

    /***
     *
     * @param chatId
     * @param username
     * @return SendMessage with the description, quantity and price of the product,
     * and the final price of the entire basket, as well as the navigation menu
     */
    private SendMessage openCartMenu(String chatId, String username) {
        float total = 0;
        StringBuilder text = new StringBuilder();
        HashMap<UUID, Integer> productsInCart = cartService.getCartByUsername(username).getProducts();
        List<Product> products = productService.getProducts(
                productsInCart.keySet().stream().toList()
        );

        for (int i = 0; i < products.size(); ++i) {
            Product product = products.get(i);
            int count = productsInCart.get(product.getId());
            total += product.getPrice() * count;
            text.append(String.format(
                    "%d) %s %.2fр. (%dшт.)\n{id:`%s`}\n",
                    i + 1,
                    product.getName(),
                    product.getPrice(),
                    count,
                    product.getId()));
        }

        if (text.isEmpty()) text.append("The cart is empty, you can fill it in the product menu.");
        else text.append(String.format("\nTotal price in cart: %.2f", total));

        return CartMenuUI.getCartMenu(chatId, text.toString());
    }

    /***
     *
     * @param callbackQuery
     * @param data
     * @param chatState used to save the chat state with the user.
     * @return SendMessage with cart menu. If interceptRequest was called when the button was clicked then
     * the request is returned to enter the data necessary to perform the selected operation
     */
    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String username = callbackQuery.getFrom().getUserName();
        String chatId = message.getChatId().toString();

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
