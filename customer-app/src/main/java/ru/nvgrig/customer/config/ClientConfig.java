package ru.nvgrig.customer.config;

import de.codecentric.boot.admin.client.config.ClientProperties;
import de.codecentric.boot.admin.client.registration.ReactiveRegistrationClient;
import de.codecentric.boot.admin.client.registration.RegistrationClient;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nvgrig.customer.client.WebClientFavoriteProductsClient;
import ru.nvgrig.customer.client.WebClientProductReviewsClient;
import ru.nvgrig.customer.client.WebClientProductsClient;

@Configuration
public class ClientConfig {

    @Bean
    @LoadBalanced
    @Scope("prototype")
    public WebClient.Builder marketServicesWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
            ObservationRegistry observationRegistry
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention())
                .filter(filter);
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${market.service.catalogue.uri:http://localhost:8081}") String catalogueBaseUrl,
            WebClient.Builder marketServicesWebClientBuilder
    ) {
        return new WebClientProductsClient(marketServicesWebClientBuilder
                .baseUrl(catalogueBaseUrl)
                .build());
    }

    @Bean
    public WebClientFavoriteProductsClient webClientFavoriteProductsClient(
            @Value("${market.service.feedback.uri:http://localhost:8084}") String feedbackBaseUrl,
            WebClient.Builder marketServicesWebClientBuilder
    ) {
        return new WebClientFavoriteProductsClient(marketServicesWebClientBuilder
                .baseUrl(feedbackBaseUrl)
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${market.service.feedback.uri:http://localhost:8084}") String feedbackBaseUrl,
            WebClient.Builder marketServicesWebClientBuilder
    ) {
        return new WebClientProductReviewsClient(marketServicesWebClientBuilder
                .baseUrl(feedbackBaseUrl)
                .build());
    }

    @Bean
    @ConditionalOnProperty(name = "spring.boot.admin.client.enabled", havingValue = "true")
    public RegistrationClient registrationClient(
            ClientProperties clientProperties,
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                authorizedClientService));
        filter.setDefaultClientRegistrationId("metrics");

        return new ReactiveRegistrationClient(WebClient.builder()
                .filter(filter)
                .build(), clientProperties.getReadTimeout());
    }
}
