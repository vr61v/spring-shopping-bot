package com.vr61v.SpringShoppingBot;

import com.vr61v.SpringShoppingBot.config.BotConfig;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.interceptor.ProductRequestInterceptor;
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

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    private final ProductRequestInterceptor productRequestInterceptor;

    private final static String HELLO_STRING = "Hello, %s %s!";

    private final static HashMap<String, UserState> chatState = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();

        if (update.hasCallbackQuery()) { // Check if the button is pressed
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = update.getCallbackQuery().getData();
            if (data.startsWith("PRODUCT_")) {
                sendMessage = productRequestInterceptor.interceptRequest(callbackQuery, data, chatState);
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
                sendMessage = productRequestInterceptor.createProduct(message.getChatId().toString(), messageText);
            }
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
                .text(String.format(HELLO_STRING, message.getFrom().getFirstName(), message.getFrom().getLastName()))
                .build();
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("Open product menu")
                .callbackData("PRODUCT_OPEN_MENU")
                .build();
        List<InlineKeyboardButton> row1 = List.of(button);
        List<List<InlineKeyboardButton>> rows = List.of(row1);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
