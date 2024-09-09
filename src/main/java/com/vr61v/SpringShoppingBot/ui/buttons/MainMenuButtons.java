package com.vr61v.SpringShoppingBot.ui.buttons;

import com.vr61v.SpringShoppingBot.entity.Emojis;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface MainMenuButtons {

    InlineKeyboardButton OPEN_PRODUCT_MENU = InlineKeyboardButton.builder()
            .text(String.format("Open product menu %s", Emojis.ARROW_RIGHT))
            .callbackData("PRODUCT_OPEN_MENU")
            .build();
    InlineKeyboardButton OPEN_CART_MENU = InlineKeyboardButton.builder()
            .text(String.format("Open cart menu %s", Emojis.ARROW_RIGHT))
            .callbackData("CART_OPEN_MENU")
            .build();
    InlineKeyboardButton OPEN_ADMIN_MENU = InlineKeyboardButton.builder()
            .text(String.format("Open admin menu %s", Emojis.ARROW_RIGHT))
            .callbackData("ADMIN_OPEN_MENU")
            .build();

    InlineKeyboardButton BACK_BUTTON = InlineKeyboardButton.builder()
            .text("Back to main menu")
            .callbackData("BACK_TO_MAIN_MENU")
            .build();

}
