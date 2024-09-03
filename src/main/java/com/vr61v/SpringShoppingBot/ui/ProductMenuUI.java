package com.vr61v.SpringShoppingBot.ui;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public interface ProductMenuUI {

    static SendMessage getProductUI(String chatId, List<String> productNames, int prevPage, int currentPage, int nextPage, int totalPage) {
        if (productNames.isEmpty()) return SendMessage.builder().chatId(chatId).text("Products not found").build();

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(parseProductNamesToMessageText(productNames))
                .parseMode(ParseMode.MARKDOWN)
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
