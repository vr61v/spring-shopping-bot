package com.vr61v.SpringShoppingBot.ui;

import com.vr61v.SpringShoppingBot.ui.buttons.CartButtons;
import com.vr61v.SpringShoppingBot.ui.buttons.MainMenuButtons;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface CartMenuUI {

    static SendMessage getCartMenu(String chatId, String cartText) {
        List<List<InlineKeyboardButton>> buttons = List.of(
                List.of(CartButtons.ADD_PRODUCT_TO_CART),
                List.of(CartButtons.REMOVE_PRODUCT_FROM_CART),
                List.of(MainMenuButtons.BACK_BUTTON)
        );

        return SendMessage.builder()
                .chatId(chatId)
                .text(cartText)
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(new InlineKeyboardMarkup(buttons))
                .build();
    }


}
