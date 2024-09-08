package com.vr61v.SpringShoppingBot.interceptor;

import com.vr61v.SpringShoppingBot.document.Product;
import com.vr61v.SpringShoppingBot.entity.UserState;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductRequestInterceptor {

    private final ProductService productService;

    private static int totalCountOfPages = 0;

    private final static int START_CURRENT_PAGE = 1;

    private final static int COUNT_OF_PRODUCT_ON_PAGE = 1;

    private final static String URL = "https://spring-shopping-bot-bucket.hb.ru-msk.vkcloud-storage.ru/%s.png";

    private SendMediaGroup openProductMenu(String chatId, int from, String query) {
        long count = productService.getProductsCount();
        totalCountOfPages = (int) Math.ceil((double) count / COUNT_OF_PRODUCT_ON_PAGE);
        int size = from + COUNT_OF_PRODUCT_ON_PAGE <= count ? COUNT_OF_PRODUCT_ON_PAGE : Math.toIntExact(count - from);

        List<Product> products;
        if (query.isEmpty()) {
            products = productService.getProductPage(from, size);
        } else {
            String[] split = query.split(":");
            products = productService.searchProductsByField(split[0], split[1], from, size);
        }

        List<String> descriptions = products.stream()
                .map(product -> String.format("%s %.2fр.\n{id:`%s`}", product.getName(), product.getPrice(), product.getId()))
                .toList();
        List<String> photos = products.stream()
                .map(product -> String.format(URL, product.getId()))
                .toList();

        return ProductMenuUI.getProductMenu(
                chatId,
                query,
                descriptions,
                photos
        );
    }

    private SendMessage openProductMenuButtons(String chatId, int current, String query) {
        return ProductMenuUI.getProductMenuButtons(
                chatId,
                query,
                current - 1,
                current,
                current + 1,
                totalCountOfPages
        );
    }

    public Pair<SendMediaGroup, SendMessage> interceptRequest(CallbackQuery callbackQuery, String data, Map<String, UserState> chatState) {
        String chatId = callbackQuery.getMessage().getChatId().toString();

        if (data.contains("OPEN_MENU")) {
            String[] request = data.split("_");
            String query = request.length > 2 ? request[2] : "";

            SendMediaGroup sendMediaGroup = openProductMenu(chatId, 0, query);
            SendMessage sendMessage = openProductMenuButtons(chatId, START_CURRENT_PAGE, query);
            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        } 
        else if (data.contains("GET_PREV_PAGE_") || data.contains("GET_NEXT_PAGE_")) {
            String[] request = data.split("_");
            int currentPage = Integer.parseInt(request[4]);
            String query = request.length > 5 ? request[5] : "";

            SendMediaGroup sendMediaGroup = openProductMenu(chatId, (currentPage - 1) * COUNT_OF_PRODUCT_ON_PAGE, query);
            SendMessage sendMessage = openProductMenuButtons(chatId, currentPage, query);
            return new ImmutablePair<>(sendMediaGroup, sendMessage);
        }
        else if (data.contains("SEARCH_BY_FIELD")) {
            chatState.put(callbackQuery.getFrom().getUserName(), UserState.PRODUCT_WAITING_QUERY);
            return new ImmutablePair<>(SendMediaGroup.builder().chatId(chatId).medias(List.of()).build(),
                    SendMessage.builder().chatId(chatId).text("Enter field:value").build());
        }

        return new ImmutablePair<>(
                SendMediaGroup.builder().chatId(chatId).build(),
                SendMessage.builder().chatId(chatId).text("Invalid request").build()
        );
    }
    
}
