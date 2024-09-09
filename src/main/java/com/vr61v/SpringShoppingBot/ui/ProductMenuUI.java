package com.vr61v.SpringShoppingBot.ui;

import com.vr61v.SpringShoppingBot.entity.Emojis;
import com.vr61v.SpringShoppingBot.ui.buttons.CartButtons;
import com.vr61v.SpringShoppingBot.ui.buttons.MainMenuButtons;
import com.vr61v.SpringShoppingBot.ui.buttons.ProductButtons;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public interface ProductMenuUI {

    static SendMediaGroup getProductMenu(String chatId, String query, List<String> descriptions, List<String> photos) {
        List<InputMedia> medias = new ArrayList<>();
        for (String url : photos) {
            medias.add(InputMediaPhoto.builder()
                    .media(url)
                    .parseMode(ParseMode.MARKDOWN)
                    .build()
            );
        }

        StringBuilder messageText = new StringBuilder();
        if (query.isEmpty()) {
            messageText.append("Products found: \n");
        } else {
            String[] split = query.split(":");
            messageText.append(String.format("Products by field %s with value %s found: \n", split[0], split[1]));
        }
        for (int i = 0; i < descriptions.size(); ++i) {
            messageText.append(i + 1)
                    .append(". ")
                    .append(descriptions.get(i))
                    .append("\n");
        }
        if (!medias.isEmpty()) medias.get(0).setCaption(messageText.toString());

        return SendMediaGroup.builder()
                .chatId(chatId)
                .medias(medias)
                .build();
    }

    static SendMessage getProductMenuButtons(String chatId, String query, int prevPage, int currentPage, int nextPage, int totalPage) {
        InlineKeyboardButton prevButton = InlineKeyboardButton.builder()
                .text(Emojis.ARROW_LEFT)
                .callbackData(String.format("PRODUCT_GET_PREV_PAGE_%s_%s", prevPage, query))
                .build();
        InlineKeyboardButton currentButton = InlineKeyboardButton.builder()
                .text(currentPage + "/" + totalPage)
                .callbackData(String.format("PRODUCT_GET_CURRENT_PAGE_%s_%s", currentPage, query))
                .build();
        InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                .text(Emojis.ARROW_RIGHT)
                .callbackData(String.format("PRODUCT_GET_NEXT_PAGE_%s_%s", nextPage, query))
                .build();

        List<InlineKeyboardButton> navigationRow = new ArrayList<>();
        if (prevPage > 0) navigationRow.add(prevButton);
        navigationRow.add(currentButton);
        if (totalPage >= nextPage) navigationRow.add(nextButton);

        List<List<InlineKeyboardButton>> buttons = List.of(
                navigationRow,
                List.of(ProductButtons.SEARCH_PRODUCT_BY_FIELD),
                List.of(CartButtons.ADD_PRODUCT_TO_CART),
                List.of(MainMenuButtons.BACK_BUTTON)
        );

        return SendMessage.builder()
                .chatId(chatId)
                .text("buttons")
                .replyMarkup(new InlineKeyboardMarkup(buttons))
                .build();
    }

}
