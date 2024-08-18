package com.vr61v.SpringShoppingBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class SpringShoppingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringShoppingBotApplication.class, args);
	}

}
