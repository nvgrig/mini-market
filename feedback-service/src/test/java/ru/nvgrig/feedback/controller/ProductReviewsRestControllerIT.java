package ru.nvgrig.feedback.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.nvgrig.feedback.entity.ProductReview;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductReviewsRestControllerIT {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    void setUp() {
        reactiveMongoTemplate.insertAll(List.of(
                new ProductReview(UUID.fromString("ca8b0377-c50d-4e95-bcb1-d9d21f6c8d12"), 1, 1, "Отзыв 1", "user-1"),
                new ProductReview(UUID.fromString("5486aed2-761e-4211-b5dc-9f9e6ae70f9f"), 1, 3, "Отзыв 2", "user-2"),
                new ProductReview(UUID.fromString("b5c37ed9-df5c-4066-b471-d4be51c7a943"), 1, 5, "Отзыв 3", "user-3")
        )).blockLast();
    }

    @AfterEach
    void tearDown() {
        reactiveMongoTemplate.remove(ProductReview.class).all()
                .block();
    }

    @Test
    void findProductReviewsByProductId_ReturnsReview() {
        webTestClient.mutateWith(mockJwt())
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        [
                          {"id" : "ca8b0377-c50d-4e95-bcb1-d9d21f6c8d12", "productId" : 1, "rating" : 1, "review" : "Отзыв 1","userId" : "user-1"},
                          {"id" : "5486aed2-761e-4211-b5dc-9f9e6ae70f9f", "productId" : 1, "rating" : 3, "review" : "Отзыв 2","userId" : "user-2"},
                          {"id" : "b5c37ed9-df5c-4066-b471-d4be51c7a943", "productId" : 1, "rating" : 5, "review" : "Отзыв 3","userId" : "user-3"}
                        ]
                        """);
    }

    @Test
    void createProductReview_RequestIsValid_ReturnsCreatedProductReview() {
        webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                            {
                              "productId" : 1,
                              "rating" : 5,
                              "review" : "Отлично"
                            }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                            {
                              "productId" : 1,
                              "rating" : 5,
                              "review" : "Отлично",
                              "userId" : "user-tester"
                            }
                        """).jsonPath("$.id").exists();
    }

    @Test
    void createProductReview_RequestIsNotValid_ReturnsBadRequest() {
        webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                            {
                              "productId" : null,
                              "rating" : -1,
                              "review" : "Отлично"
                            }
                        """)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().doesNotExist(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .json("""
                            {
                              "errors" : [
                                "Оценка меньше 1"
                              ]
                            }
                        """);
    }

    @Test
    void findProductReviewsByProductId_UserIsNotAuthenticated_ReturnsNotAuthorized() {
        webTestClient
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}