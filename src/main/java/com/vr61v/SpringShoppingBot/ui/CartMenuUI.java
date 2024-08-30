package com.vr61v.SpringShoppingBot.ui;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface CartMenuUI {

    static SendMessage getCardUI(String chatId, String cartText) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(cartText)
                .parseMode(ParseMode.MARKDOWN)
                .build();

        InlineKeyboardButton addProductToCart = InlineKeyboardButton.builder()
                .text("Add product to cart")
                .callbackData("CART_ADD_TO")
                .build();
        InlineKeyboardButton removeProductFromCart = InlineKeyboardButton.builder()
                .text("Remove product from cart")
                .callbackData("CART_REMOVE_FROM")
                .build();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Back")
                .callbackData("BACK_TO_MAIN_MENU")
                .build();

        List<List<InlineKeyboardButton>> buttons = List.of(
                List.of(addProductToCart),
                List.of(removeProductFromCart),
                List.of(backButton)
        );

        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
        return sendMessage;
    }


}
