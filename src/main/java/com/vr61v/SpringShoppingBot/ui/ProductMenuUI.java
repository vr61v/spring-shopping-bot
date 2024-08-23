package com.vr61v.SpringShoppingBot.ui;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public interface ProductMenuUI {

    static SendMessage getProductUI(String chatId, List<String> productNames, int prevPage, int currentPage, int nextPage, int totalPage) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(parseProductNamesToMessageText(productNames))
                .build();

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
        InlineKeyboardButton addProductToBucket = InlineKeyboardButton.builder()
                .text("Add product to bucket")
                .callbackData("PRODUCT_ADD_TO_BUCKET")
                .build();
        InlineKeyboardButton addProductToFavorites = InlineKeyboardButton.builder()
                .text("Add product to favorites")
                .callbackData("PRODUCT_ADD_TO_FAVORITES")
                .build();
        InlineKeyboardButton createProduct = InlineKeyboardButton.builder()
                .text("Create new product (only for admin)")
                .callbackData("PRODUCT_CREATE")
                .build();

        List<InlineKeyboardButton> navigationRow = new ArrayList<>();
        if (prevPage > 0) navigationRow.add(prevButton);
        navigationRow.add(currentButton);
        if (totalPage >= nextPage) navigationRow.add(nextButton);

        List<List<InlineKeyboardButton>> buttons = List.of(
                navigationRow,
                List.of(addProductToBucket),
                List.of(addProductToFavorites),
                List.of(createProduct)
        );

        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
        return sendMessage;
    }

    static String parseProductNamesToMessageText(List<String> productNames) {
        StringBuilder messageText = new StringBuilder();
        messageText.append("Products:\n");
        for (int i = 0; i < productNames.size(); ++i) {
            messageText.append(i + 1).append(". ").append(productNames.get(i)).append("\n");
        }
        return messageText.toString();
    }

}
