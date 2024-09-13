package com.vr61v.SpringShoppingBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Value("LIST_OF_BOT_ADMINS")
    private List<String> admins;

}
