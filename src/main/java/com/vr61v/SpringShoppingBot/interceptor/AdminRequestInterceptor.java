package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.ui.AdminMenuUI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminRequestInterceptor {

    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();

        if (data.contains("ADMIN_OPEN_MENU")){
            return openAdminMenu(chatId);
        } else if (data.contains("PRODUCT_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_CREATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter new product fields").build();
        } else if (data.contains("PRODUCT_UPDATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_UPDATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter product fields for update").build();
        } else if (data.contains("PRODUCT_SEARCH")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_SEARCH_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter product's name for search").build();
        } else if (data.contains("PRODUCT_DELETE")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_DELETE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter product's id for delete").build();
        }

        else if (data.contains("CATEGORY_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.CATEGORY_WAITING_CREATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter new category data").build();
        } else if (data.contains("CATEGORY_UPDATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.CATEGORY_WAITING_UPDATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter product fields for update").build();
        } else if (data.contains("CATEGORY_SEARCH")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.CATEGORY_WAITING_SEARCH_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter category's name for search").build();
        } else if (data.contains("CATEGORY_DELETE")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.CATEGORY_WAITING_DELETE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter category's id for delete").build();
        }

        else if (data.contains("VENDOR_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.VENDOR_WAITING_CREATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter new vendor data").build();
        } else if (data.contains("VENDOR_UPDATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.VENDOR_WAITING_UPDATE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter vendor fields for update").build();
        } else if (data.contains("VENDOR_SEARCH")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.VENDOR_WAITING_SEARCH_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter vendor's name for search").build();
        } else if (data.contains("VENDOR_DELETE")){
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.VENDOR_WAITING_DELETE_REQUEST);
            return SendMessage.builder().chatId(chatId).text("Enter vendor's id for delete").build();
        }

        return SendMessage.builder().chatId(chatId).text("Invalid request").build();
    }

    private SendMessage openAdminMenu(String chatId) {
        return AdminMenuUI.getAdminUI(chatId);
    }

}
