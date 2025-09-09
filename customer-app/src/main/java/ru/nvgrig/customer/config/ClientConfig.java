package ru.nvgrig.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nvgrig.customer.client.WebClientFavoriteProductsClient;
import ru.nvgrig.customer.client.WebClientProductReviewsClient;
import ru.nvgrig.customer.client.WebClientProductsClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${market.service.catalogue.uri:http://localhost:8081}") String catalogueBaseUrl
    ) {
        return new WebClientProductsClient(WebClient.builder()
                .baseUrl(catalogueBaseUrl)
                .build());
    }

    @Bean
    public WebClientFavoriteProductsClient webClientFavoriteProductsClient(
            @Value("${market.service.feedback.uri:http://localhost:8084}") String feedbackBaseUrl
    ) {
        return new WebClientFavoriteProductsClient(WebClient.builder()
                .baseUrl(feedbackBaseUrl)
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${market.service.review.uri:http://localhost:8084}") String feedbackBaseUrl
    ) {
        return new WebClientProductReviewsClient(WebClient.builder()
                .baseUrl(feedbackBaseUrl)
                .build());
    }
}
