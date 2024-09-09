package com.vr61v.SpringShoppingBot;

import com.vr61v.SpringShoppingBot.config.BotConfig;
import com.vr61v.SpringShoppingBot.controller.CartController;
import com.vr61v.SpringShoppingBot.controller.CategoryController;
import com.vr61v.SpringShoppingBot.controller.ProductController;
import com.vr61v.SpringShoppingBot.controller.VendorController;
import com.vr61v.SpringShoppingBot.entity.Emojis;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.interceptor.AdminRequestInterceptor;
import com.vr61v.SpringShoppingBot.interceptor.CartRequestInterceptor;
import com.vr61v.SpringShoppingBot.interceptor.ProductRequestInterceptor;
import com.vr61v.SpringShoppingBot.ui.buttons.MainMenuButtons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    private final AdminRequestInterceptor adminRequestInterceptor;

    private final ProductRequestInterceptor productRequestInterceptor;

    private final CartRequestInterceptor cartRequestInterceptor;

    private final ProductController productController;

    private final CategoryController categoryController;

    private final VendorController vendorController;

    private final CartController cartController;

    private final static HashMap<String, UserState> chatState = new HashMap<>();

    /***
     *
     * @param entity presentation of an object as a string in the form of a field:value\nfield:value...
     * @return map with string key (field) and string value.
     */
    private Map<String, String> parseMessageToField(String entity) {
        List<String> lines = List.of(entity.split("\n"));
        Map<String, String> map = new HashMap<>();
        for (String line : lines) {
            String[] keyValue = line.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    private void sendInvalidRequest(String chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Invalid request, enter `/start` to get main menu")
                .parseMode(ParseMode.MARKDOWN)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void startBotCommand(Update update) {
        String chatId = (update.hasCallbackQuery() ?
                update.getCallbackQuery().getMessage() : update.getMessage())
                .getChatId().toString();
        String username = update.hasCallbackQuery() ?
                update.getCallbackQuery().getFrom().getUserName() :
                update.getMessage().getFrom().getUserName();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(MainMenuButtons.OPEN_PRODUCT_MENU));
        buttons.add(List.of(MainMenuButtons.OPEN_CART_MENU));
        if (config.getAdmins().contains(username)) buttons.add(List.of(MainMenuButtons.OPEN_ADMIN_MENU));

        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Hello! Welcome to Spring Shopping Bot!")
                    .replyMarkup(new InlineKeyboardMarkup(buttons))
                    .build()
            );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponseOnProductCallbackQuery(CallbackQuery callbackQuery, String data) {
        Pair<SendMediaGroup, SendMessage> response = productRequestInterceptor.interceptRequest(callbackQuery, data, chatState);
        try {
            int size = response.getLeft().getMedias().size();
            if (2 <= size && size <= 10) execute(response.getLeft());
            else if (size == 1) {
                SendMediaGroup group = response.getLeft();
                String chatId = group.getChatId();
                String caption = group.getMedias().get(0).getCaption();
                String url = group.getMedias().get(0).getMedia();

                InputStream stream = new URL(url).openStream();
                InputFile photo = new InputFile(stream, url);
                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(chatId)
                        .photo(photo)
                        .caption(caption)
                        .parseMode(ParseMode.MARKDOWN)
                        .build();

                execute(sendPhoto);
                log.info("Success sending product menu photos: {}", sendPhoto);
            }
            execute(response.getRight());
            log.info("Success sending product menu buttons: {}", response.getRight());
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            sendInvalidRequest(String.valueOf(callbackQuery.getMessage().getChatId()));
            log.error("Error sending product menu: {}", e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasCallbackQuery()) { // Check if the button is pressed
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();

            if (data.startsWith("PRODUCT_")) {
                sendResponseOnProductCallbackQuery(callbackQuery, data);
                return;
            } else if (data.equals("BACK_TO_MAIN_MENU")) {
                startBotCommand(update);
                return;
            } else if (data.startsWith("CART_")) {
                sendMessage = cartRequestInterceptor.interceptRequest(callbackQuery, data, chatState);
            } else if (data.startsWith("ADMIN_")) {
                sendMessage = adminRequestInterceptor.interceptRequest(callbackQuery, data, chatState);
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) { // Check if the command is called
            Message message = update.getMessage();
            String messageText = message.getText();
            String username = update.getMessage().getFrom().getUserName();
            UserState userState = chatState.get(username);

            if (messageText.equals("/start")) {
                startBotCommand(update);
            }

            else if (userState.equals(UserState.PRODUCT_WAITING_CREATE_REQUEST)) {
                chatState.remove(username);
                sendMessage = productController.createProduct(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState.equals(UserState.PRODUCT_WAITING_UPDATE_REQUEST)) {
                chatState.put(username, userState);
                sendMessage = productController.updateProduct(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState.equals(UserState.PRODUCT_WAITING_SEARCH_REQUEST)) {
                chatState.remove(username);
                sendMessage = productController.searchProducts(message.getChatId().toString(), messageText);
            } else if (userState.equals(UserState.PRODUCT_WAITING_DELETE_REQUEST)) {
                chatState.remove(username);
                sendMessage = productController.deleteProduct(message.getChatId().toString(), messageText);
            } else if (userState.equals(UserState.PRODUCT_WAITING_QUERY)) {
                chatState.remove(username);
                CallbackQuery callbackQuery = new CallbackQuery(
                        null,
                        message.getFrom(),
                        message,
                        null,
                        String.format("OPEN_MENU_%s", messageText),
                        null,
                        null);
                String data = callbackQuery.getData();
                sendResponseOnProductCallbackQuery(callbackQuery, data);
                return;
            }

            else if (userState.equals(UserState.CATEGORY_WAITING_CREATE_REQUEST)) {
                chatState.remove(username);
                sendMessage = categoryController.createCategory(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState.equals(UserState.CATEGORY_WAITING_UPDATE_REQUEST)) {
                chatState.put(username, userState);
                sendMessage = categoryController.updateCategory(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState.equals(UserState.CATEGORY_WAITING_SEARCH_REQUEST)) {
                chatState.remove(username);
                sendMessage = categoryController.searchCategory(message.getChatId().toString(), messageText);
            } else if (userState.equals(UserState.CATEGORY_WAITING_DELETE_REQUEST)) {
                chatState.remove(username);
                sendMessage = categoryController.deleteCategory(message.getChatId().toString(), messageText);
            }

            else if (userState.equals(UserState.VENDOR_WAITING_CREATE_REQUEST)) {
                chatState.remove(username);
                sendMessage = vendorController.createVendor(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState.equals(UserState.VENDOR_WAITING_UPDATE_REQUEST)) {
                chatState.put(username, userState);
                sendMessage = vendorController.updateVendor(message.getChatId().toString(), parseMessageToField(messageText));
            } else if (userState.equals(UserState.VENDOR_WAITING_SEARCH_REQUEST)) {
                chatState.remove(username);
                sendMessage = vendorController.searchVendor(message.getChatId().toString(), messageText);
            } else if (userState.equals(UserState.VENDOR_WAITING_DELETE_REQUEST)) {
                chatState.remove(username);
                sendMessage = vendorController.deleteVendor(message.getChatId().toString(), messageText);
            }

            else if (userState.equals(UserState.CART_WAITING_ADD_IN)) {
                chatState.remove(username);
                sendMessage = cartController.addProductInCart(message.getChatId().toString(), message.getFrom().getUserName(), messageText);
            } else if (userState.equals(UserState.CART_WAITING_REMOVE_FROM)) {
                chatState.remove(username);
                sendMessage = cartController.removeProductFromCart(message.getChatId().toString(), message.getFrom().getUserName(), messageText);
            }
        } else {
            sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text("Unknown command, try again")
                    .build();
            log.warn("Unknown command: {}", update);
        }

        try {
            execute(sendMessage);
            log.info("Success sending message: {}", sendMessage);
        } catch (TelegramApiException e) {
            sendInvalidRequest(
                    (update.hasCallbackQuery() ? update.getCallbackQuery().getMessage() : update.getMessage())
                    .getChatId().toString()
            );
            log.error("Error sending message {}: {}", sendMessage, e.getMessage());
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

}
