package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.controller.impl.ProductControllerTelegramBot;
import com.vr61v.SpringShoppingBot.document.request.product.CreateProductRequest;
import com.vr61v.SpringShoppingBot.entity.UserState;
import com.vr61v.SpringShoppingBot.ui.ProductMenuUI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRequestInterceptor {

    private final ProductControllerTelegramBot productController;

    private final static int START_PREV_PAGE = 0;
    private final static int START_CURRENT_PAGE = 1;
    private final static int START_NEXT_PAGE = 2;

    public SendMessage interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();

        if (data.contains("PRODUCT_OPEN_MENU")) {
            return openProductMenu(chatId, data);
        } else if (data.contains("PRODUCT_GET_PREV_PAGE_")) {
            return getPrevPage(chatId, data);
        } else if (data.contains("PRODUCT_GET_NEXT_PAGE_")) {
            return getNextPage(chatId, data);
        } else if (data.contains("PRODUCT_CREATE")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_CREATE_REQUEST );
            return SendMessage.builder().chatId(chatId).text("Enter new product data").build();
        }

        return SendMessage.builder().text("Invalid request").build();
    }

    public SendMessage openProductMenu(String chatId, String data) {
        List<String> productNames = List.of("dick", "big cock", "condoms");
        int totalPage = 10;
        return ProductMenuUI.getProductUI(
                chatId,
                productNames,
                START_PREV_PAGE,
                START_CURRENT_PAGE,
                START_NEXT_PAGE,
                totalPage
        );
    }

    public SendMessage getPrevPage(String chatId, String data) {
        List<String> productNames = List.of("dick", "big cock", "condoms");
        int newCurrentPage = Integer.parseInt(data.split("GET_PREV_PAGE_")[1]);
        int newPrevPage = newCurrentPage - 1;
        int newNextPage = newCurrentPage + 1;
        int totalPage = 10;
        return ProductMenuUI.getProductUI(
                chatId,
                productNames,
                newPrevPage,
                newCurrentPage,
                newNextPage,
                totalPage
        );
    }

    public SendMessage getNextPage(String chatId, String data) {
        List<String> productNames = List.of("dick", "big cock", "condoms");
        int newCurrentPage = Integer.parseInt(data.split("GET_NEXT_PAGE_")[1]);
        int newPrevPage = newCurrentPage - 1;
        int newNextPage = newCurrentPage + 1;
        int totalPage = 10;
        return ProductMenuUI.getProductUI(
                chatId,
                productNames,
                newPrevPage,
                newCurrentPage,
                newNextPage,
                totalPage
        );
    }


    public SendMessage createProduct(String chatId, String product) {
        List<String> lines = List.of(product.split("\n"));
        Map<String, String> map = new HashMap<>();
        for (String line : lines) {
            String[] keyValue = line.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        String name = map.get("name");
        String price = map.get("price");
        String count = map.get("count");
        String description = map.get("description");
        String category = map.get("category");
        String vendor = map.get("vendor");

        // Get category and vendor and search id in database
        // if not exists then create new category
        // ...

        UUID categoryId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();

        CreateProductRequest createProductRequest = new CreateProductRequest(
                name,
                price != null ? Float.parseFloat(price) : 0,
                count != null ? Integer.parseInt(count) : 0,
                description,
                categoryId,
                vendorId
        );

        ResponseEntity<?> response = productController.createProduct(createProductRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            return SendMessage.builder().chatId(chatId).text("Product created: " + response.getBody()).build();
        }
        return SendMessage.builder().chatId(chatId).text("Invalid request: " + response.getBody()).build();
    }

}
