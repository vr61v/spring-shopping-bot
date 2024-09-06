package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.service.ProductService;
import com.vr61v.SpringShoppingBot.ui.ProductMenuUI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRequestInterceptor {

    private final ProductService productService;

    private static int totalCountOfPages = 0;

    private final static int START_CURRENT_PAGE = 1;

    private final static int COUNT_OF_PRODUCT_ON_PAGE = 5;

    private final static String URL = "https://spring-shopping-bot-bucket.hb.ru-msk.vkcloud-storage.ru/%s.png";

    private SendMediaGroup openProductMenu(String chatId, int from) {
        long count = productService.getProductsCount();
        totalCountOfPages = (int) Math.ceil((double) count / COUNT_OF_PRODUCT_ON_PAGE);
        List<Product> products = productService.getProductPage(
                from, from + COUNT_OF_PRODUCT_ON_PAGE <= count ? COUNT_OF_PRODUCT_ON_PAGE : Math.toIntExact(count - from));

        List<String> descriptions = products.stream()
                .map(product -> String.format("%s %.2fр. \n{id:`%s`}", product.getName(), product.getPrice(), product.getId()))
                .toList();
        List<String> photos = products.stream()
                .map(product -> String.format(URL, product.getId()))
                .toList();

        return ProductMenuUI.getProductMenu(
                chatId,
                descriptions,
                photos
        );
    }

    private SendMessage openProductMenuButtons(String chatId, int current) {
        return ProductMenuUI.getProductMenuButtons(
                chatId,
                current - 1,
                current,
                current + 1,
                totalCountOfPages
        );
    }


    public Pair<SendMediaGroup, SendMessage> interceptRequest(CallbackQuery callbackQuery, String data) {
        String chatId = callbackQuery.getMessage().getChatId().toString();

        if (data.contains("OPEN_MENU")) {
            SendMediaGroup sendMediaGroup = openProductMenu(chatId, 0);
            SendMessage sendMessage = openProductMenuButtons(chatId, START_CURRENT_PAGE);
            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        } 
        else if (data.contains("GET_PREV_PAGE_")) {
            int currentPage = Integer.parseInt(data.split("GET_PREV_PAGE_")[1]);
            SendMediaGroup sendMediaGroup = openProductMenu(chatId, (currentPage - 1) * COUNT_OF_PRODUCT_ON_PAGE);
            SendMessage sendMessage = openProductMenuButtons(chatId, currentPage);
            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        } 
        else if (data.contains("GET_NEXT_PAGE_")) {
            int currentPage = Integer.parseInt(data.split("GET_NEXT_PAGE_")[1]);
            SendMediaGroup sendMediaGroup = openProductMenu(chatId, (currentPage - 1) * COUNT_OF_PRODUCT_ON_PAGE);
            SendMessage sendMessage = openProductMenuButtons(chatId, currentPage);
            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        }

        return new ImmutablePair<>(
                SendMediaGroup.builder().chatId(chatId).build(),
                SendMessage.builder().chatId(chatId).text("Invalid request").build()
        );
    }
    
}
