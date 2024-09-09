package com.vr61v.SpringShoppingBot.ui.buttons;

import com.vr61v.SpringShoppingBot.entity.Emojis;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ProductButtons {

    InlineKeyboardButton SEARCH_PRODUCT_BY_FIELD = InlineKeyboardButton.builder()
            .text(String.format("Search product by field and value %s", Emojis.MAG_RIGHT))
            .callbackData("PRODUCT_SEARCH_BY_FIELD")
            .build();

}
