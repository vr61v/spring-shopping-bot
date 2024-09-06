package com.vr61v.SpringShoppingBot.ui;

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

    static SendMediaGroup getProductMenu(String chatId, List<String> descriptions, List<String> photos) {
        List<InputMedia> medias = new ArrayList<>();
        for (String url : photos) {
            medias.add(InputMediaPhoto.builder()
                    .media(url)
                    .parseMode(ParseMode.MARKDOWN)
                    .build()
            );
        }

        StringBuilder messageText = new StringBuilder();
        for (int i = 0; i < descriptions.size(); ++i) {
            messageText.append(i + 1)
                    .append(". ")
                    .append(descriptions.get(i))
                    .append("\n");
        }
        medias.get(0).setCaption(messageText.toString());

        return SendMediaGroup.builder()
                .chatId(chatId)
                .medias(medias)
                .build();
    }

    static SendMessage getProductMenuButtons(String chatId, int prevPage, int currentPage, int nextPage, int totalPage) {
        InlineKeyboardButton prevButton = InlineKeyboardButton.builder()
                .text("<")
                .callbackData("PRODUCT_GET_PREV_PAGE_" + prevPage)
                .build();
        InlineKeyboardButton currentButton = InlineKeyboardButton.builder()
                .text(currentPage + "/" + totalPage)
                .callbackData("PRODUCT_GET_CURRENT_PAGE_" + currentPage)
                .build();
        InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                .text(">")
                .callbackData("PRODUCT_GET_NEXT_PAGE_" + nextPage)
                .build();
        InlineKeyboardButton addProductToCart = InlineKeyboardButton.builder()
                .text("Add product to cart")
                .callbackData("CART_ADD_TO")
                .build();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Back")
                .callbackData("BACK_TO_MAIN_MENU")
                .build();

        List<InlineKeyboardButton> navigationRow = new ArrayList<>();
        if (prevPage > 0) navigationRow.add(prevButton);
        navigationRow.add(currentButton);
        if (totalPage >= nextPage) navigationRow.add(nextButton);

        List<List<InlineKeyboardButton>> buttons = List.of(
                navigationRow,
                List.of(addProductToCart),
                List.of(backButton)
        );

        return SendMessage.builder()
                .chatId(chatId)
                .text("buttons")
                .replyMarkup(new InlineKeyboardMarkup(buttons))
                .build();
    }

}
