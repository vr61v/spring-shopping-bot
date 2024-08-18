package com.vr61v.SpringShoppingBot.config;

import org.opensearch.client.RestHighLevelClient;
import org.opensearch.data.client.orhlc.AbstractOpenSearchConfiguration;
import org.opensearch.data.client.orhlc.ClientConfiguration;
import org.opensearch.data.client.orhlc.RestClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpHeaders;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class RestClientConfig extends AbstractOpenSearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient opensearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}