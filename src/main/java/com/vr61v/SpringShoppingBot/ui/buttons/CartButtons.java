package com.vr61v.SpringShoppingBot.ui.buttons;

import com.vr61v.SpringShoppingBot.entity.Emojis;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface CartButtons {

    InlineKeyboardButton ADD_PRODUCT_TO_CART = InlineKeyboardButton.builder()
            .text(String.format("Add product to cart %s", Emojis.SHOPPING_TROLLEY))
            .callbackData("CART_ADD_TO")
            .build();

    InlineKeyboardButton REMOVE_PRODUCT_FROM_CART = InlineKeyboardButton.builder()
            .text(String.format("Remove product from cart %s", Emojis.WASTEBASKET))
            .callbackData("CART_REMOVE_FROM")
            .build();

}
