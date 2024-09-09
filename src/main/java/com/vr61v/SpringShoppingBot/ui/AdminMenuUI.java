package com.vr61v.SpringShoppingBot.ui;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface AdminMenuUI {

    static SendMessage getAdminMenu(String chatId) {
        InlineKeyboardButton createProduct = InlineKeyboardButton.builder()
                .text("Create product")
                .callbackData("ADMIN_PRODUCT_CREATE")
                .build();
        InlineKeyboardButton updateProduct = InlineKeyboardButton.builder()
                .text("Update product")
                .callbackData("ADMIN_PRODUCT_UPDATE")
                .build();
        InlineKeyboardButton searchProduct = InlineKeyboardButton.builder()
                .text("Search product")
                .callbackData("ADMIN_PRODUCT_SEARCH")
                .build();
        InlineKeyboardButton deleteProduct = InlineKeyboardButton.builder()
                .text("Delete product")
                .callbackData("ADMIN_PRODUCT_DELETE")
                .build();

        InlineKeyboardButton createCategory = InlineKeyboardButton.builder()
                .text("Create category")
                .callbackData("ADMIN_CATEGORY_CREATE")
                .build();
        InlineKeyboardButton updateCategory = InlineKeyboardButton.builder()
                .text("Update category")
                .callbackData("ADMIN_CATEGORY_UPDATE")
                .build();
        InlineKeyboardButton searchCategory = InlineKeyboardButton.builder()
                .text("Search category")
                .callbackData("ADMIN_CATEGORY_SEARCH")
                .build();
        InlineKeyboardButton deleteCategory = InlineKeyboardButton.builder()
                .text("Delete category")
                .callbackData("ADMIN_CATEGORY_DELETE")
                .build();

        InlineKeyboardButton createVendor = InlineKeyboardButton.builder()
                .text("Create vendor")
                .callbackData("ADMIN_VENDOR_CREATE")
                .build();
        InlineKeyboardButton updateVendor = InlineKeyboardButton.builder()
                .text("Update vendor")
                .callbackData("ADMIN_VENDOR_UPDATE")
                .build();
        InlineKeyboardButton searchVendor = InlineKeyboardButton.builder()
                .text("Search vendor")
                .callbackData("ADMIN_VENDOR_SEARCH")
                .build();
        InlineKeyboardButton deleteVendor = InlineKeyboardButton.builder()
                .text("Delete vendor")
                .callbackData("ADMIN_VENDOR_DELETE")
                .build();

        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Back")
                .callbackData("BACK_TO_MAIN_MENU")
                .build();

        List<List<InlineKeyboardButton>> buttons = List.of(
                List.of(createProduct, updateProduct),
                List.of(searchProduct, deleteProduct),
                List.of(createCategory, updateCategory),
                List.of(searchCategory, deleteCategory),
                List.of(createVendor, updateVendor),
                List.of(searchVendor, deleteVendor),
                List.of(backButton)
        );

        return SendMessage.builder()
                .chatId(chatId)
                .text("Welcome to admin menu!")
                .replyMarkup(new InlineKeyboardMarkup(buttons))
                .build();
    }

}