package ru.nvgrig.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.nvgrig.manager.client.RestClientProductsRestClient;

@Configuration
public class ClientBeans {
    @Bean
    public RestClientProductsRestClient restClientProductsRestClient(
            @Value("${market.service.catalogue.uri:http://localhost:8081}") String catalogueBaseUrl) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUrl)
                .build());
    }
}
