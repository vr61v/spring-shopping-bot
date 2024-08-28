package com.vr61v.SpringShoppingBot;

import com.vr61v.SpringShoppingBot.config.BotConfig;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.interceptor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    private final ProductRequestInterceptor productRequestInterceptor;

    private final AdminRequestInterceptor adminRequestInterceptor;

    private final AdminProductRequestInterceptor adminProductRequestInterceptor;

    private final AdminCategoryRequestInterceptor adminCategoryRequestInterceptor;

    private final AdminVendorRequestInterceptor adminVendorRequestInterceptor;

    private final static HashMap<String, UserState> chatState = new HashMap<>();

    private Map<String, String> parseMessageToField(String entity) {
        List<String> lines = List.of(entity.split("\n"));
        Map<String, String> map = new HashMap<>();
        for (String line : lines) {
            String[] keyValue = line.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();

        if (update.hasCallbackQuery()) { // Check if the button is pressed
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = update.getCallbackQuery().getData();
            if (data.startsWith("PRODUCT_")) {
                sendMessage = productRequestInterceptor.interceptRequest(callbackQuery, data, chatState);
            } else if (data.startsWith("ADMIN_")) {
                sendMessage = adminRequestInterceptor.interceptRequest(callbackQuery, data, chatState);
            } else if (data.equals("BACK_TO_MAIN_MENU")) {
                update.setMessage(update.getCallbackQuery().getMessage());
                startBotCommand(update);
                return;
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) { // Check if the command is called
            Message message = update.getMessage();
            String messageText = message.getText();
            String username = update.getMessage().getFrom().getUserName();
            UserState userState = chatState.get(username);
            if (messageText.equals("/start")) {
                startBotCommand(update);
            } else if (userState == UserState.PRODUCT_WAITING_CREATE_REQUEST) {
                chatState.remove(username);
                sendMessage = adminProductRequestInterceptor.createProduct(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState == UserState.PRODUCT_WAITING_UPDATE_REQUEST) {
                chatState.put(username, userState);
                sendMessage = adminProductRequestInterceptor.updateProduct(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState == UserState.PRODUCT_WAITING_SEARCH_REQUEST) {
                chatState.remove(username);
                sendMessage = adminProductRequestInterceptor.searchProducts(message.getChatId().toString(), messageText);
            } else if (userState == UserState.PRODUCT_WAITING_DELETE_REQUEST) {
                chatState.remove(username);
                sendMessage = adminProductRequestInterceptor.deleteProduct(message.getChatId().toString(), messageText);
            }

            else if (userState == UserState.CATEGORY_WAITING_CREATE_REQUEST) {
                chatState.remove(username);
                sendMessage = adminCategoryRequestInterceptor.createCategory(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState == UserState.CATEGORY_WAITING_UPDATE_REQUEST) {
                chatState.put(username, userState);
                sendMessage = adminCategoryRequestInterceptor.updateCategory(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState == UserState.CATEGORY_WAITING_SEARCH_REQUEST) {
                chatState.remove(username);
                sendMessage = adminCategoryRequestInterceptor.searchCategory(message.getChatId().toString(), messageText);
            } else if (userState == UserState.CATEGORY_WAITING_DELETE_REQUEST) {
                chatState.remove(username);
                sendMessage = adminCategoryRequestInterceptor.deleteCategory(message.getChatId().toString(), messageText);
            }

            else if (userState == UserState.VENDOR_WAITING_CREATE_REQUEST) {
                chatState.remove(username);
                sendMessage = adminVendorRequestInterceptor.createVendor(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState == UserState.VENDOR_WAITING_UPDATE_REQUEST) {
                chatState.put(username, userState);
                sendMessage = adminVendorRequestInterceptor.updateVendor(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState == UserState.VENDOR_WAITING_SEARCH_REQUEST) {
                chatState.remove(username);
                sendMessage = adminVendorRequestInterceptor.searchVendor(message.getChatId().toString(), messageText);
            } else if (userState == UserState.VENDOR_WAITING_DELETE_REQUEST) {
                chatState.remove(username);
                sendMessage = adminVendorRequestInterceptor.deleteVendor(message.getChatId().toString(), messageText);
            }
        } else {
            sendMessage = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("Unknown command").build();
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public void startBotCommand(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("Main menu")
                .build();
        InlineKeyboardButton openProductMenu = InlineKeyboardButton.builder()
                .text("Open product menu")
                .callbackData("PRODUCT_OPEN_MENU")
                .build();
        InlineKeyboardButton openAdminMenu = InlineKeyboardButton.builder()
                .text("Open admin menu")
                .callbackData("ADMIN_OPEN_MENU")
                .build();
        List<List<InlineKeyboardButton>> buttons = List.of(
                List.of(openProductMenu),
                List.of(openAdminMenu)
        );
        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
