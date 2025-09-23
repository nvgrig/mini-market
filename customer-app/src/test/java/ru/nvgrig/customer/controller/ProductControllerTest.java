package ru.nvgrig.customer.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.ui.ConcurrentModel;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.nvgrig.customer.client.FavoriteProductsClient;
import ru.nvgrig.customer.client.ProductReviewsClient;
import ru.nvgrig.customer.client.ProductsClient;
import ru.nvgrig.customer.entity.Product;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductsClient productsClient;
    @Mock
    FavoriteProductsClient favoriteProductsClient;
    @Mock
    ProductReviewsClient productReviewsClient;
    @InjectMocks
    ProductController controller;

    @Test
    void loadProduct_ProductExists_ReturnsNotEmptyMono() {
        Product product = new Product(1, "Товар 1", "Описание товара 1");
        Mockito.when(productsClient.findProduct(1)).thenReturn(Mono.just(product));

        StepVerifier.create(controller.loadProduct(1))
                .expectNext(new Product(1, "Товар 1", "Описание товара 1"))
                .expectComplete()
                .verify();

        verify(productsClient).findProduct(1);
        verifyNoMoreInteractions(productsClient);
    }

    @Test
    void loadProduct_ProductDoesNotExists_ReturnsMonoNoSuchElementException() {
        Mockito.when(productsClient.findProduct(1)).thenReturn(Mono.empty());

        StepVerifier.create(controller.loadProduct(1))
                .expectErrorMatches(exception -> exception instanceof NoSuchElementException e &&
                        e.getMessage().equals("customer.products.error.not_found"))
                .verify();

        verify(productsClient).findProduct(1);
        verifyNoMoreInteractions(productsClient);
    }

    @Test
    @DisplayName("Исключение NoSuchElementException должно быть транслировано в страницу errors/404")
    void handleNoSuchElementException_ReturnsErrors404() {
        NoSuchElementException exception = new NoSuchElementException("Товар не найден");
        ConcurrentModel model = new ConcurrentModel();
        MockServerHttpResponse response = new MockServerHttpResponse();

        String actual = controller.handleNoSuchElementException(exception, model, response);

        assertEquals("error/404", actual);
        assertEquals("Товар не найден", model.getAttribute("error"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



  
}