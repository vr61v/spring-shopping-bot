package com.vr61v.SpringShoppingBot.ui;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface AdminMenuUI {

    static SendMessage getAdminUI(String chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Admin menu")
                .build();

        InlineKeyboardButton createProduct = InlineKeyboardButton.builder()
                .text("Create new product (only for admin)")
                .callbackData("ADMIN_PRODUCT_CREATE")
                .build();
        InlineKeyboardButton deleteProduct = InlineKeyboardButton.builder()
                .text("Delete product (only for admin)")
                .callbackData("ADMIN_PRODUCT_DELETE")
                .build();
        InlineKeyboardButton createCategory = InlineKeyboardButton.builder()
                .text("Create new category (only for admin)")
                .callbackData("ADMIN_CATEGORY_CREATE")
                .build();
        InlineKeyboardButton deleteCategory = InlineKeyboardButton.builder()
                .text("Delete category (only for admin)")
                .callbackData("ADMIN_CATEGORY_DELETE")
                .build();
        InlineKeyboardButton createVendor = InlineKeyboardButton.builder()
                .text("Create new vendor (only for admin)")
                .callbackData("ADMIN_VENDOR_CREATE")
                .build();
        InlineKeyboardButton deleteVendor = InlineKeyboardButton.builder()
                .text("Delete vendor (only for admin)")
                .callbackData("ADMIN_VENDOR_DELETE")
                .build();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Back")
                .callbackData("BACK_TO_MAIN_MENU")
                .build();

        List<List<InlineKeyboardButton>> buttons = List.of(
                List.of(createProduct),
                List.of(deleteProduct),
                List.of(createCategory),
                List.of(deleteCategory),
                List.of(createVendor),
                List.of(deleteVendor),
                List.of(backButton)
        );

        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
        return sendMessage;
    }
}