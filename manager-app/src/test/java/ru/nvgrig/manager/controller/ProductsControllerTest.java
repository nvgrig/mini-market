package ru.nvgrig.manager.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.nvgrig.manager.client.BadRequestException;
import ru.nvgrig.manager.client.ProductsRestClient;
import ru.nvgrig.manager.controller.payload.NewProductPayload;
import ru.nvgrig.manager.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты ProductsController")
class ProductsControllerTest {

    @Mock
    ProductsRestClient productsRestClient;
    @InjectMocks
    ProductsController controller;

    @Test
    @DisplayName("createProduct создаст новый товар и перенаправит на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        NewProductPayload payload = new NewProductPayload("Новый товар", "Описание нового товара");
        ConcurrentModel model = new ConcurrentModel();

        doReturn(new Product(1, "Новый товар", "Описание нового товара"))
                .when(productsRestClient)
                .createProduct("Новый товар", "Описание нового товара");

        String actual = controller.createProduct(payload, model);

        assertEquals("redirect:/catalogue/products/1", actual);
        verify(productsRestClient).createProduct("Новый товар", "Описание нового товара");
        verifyNoMoreInteractions(productsRestClient);
    }

    @Test
    @DisplayName("createProduct вернет страницу с ошибками, если запрос невалиден")
    void createProduct_RequestIsInvalid_ReturnsProductsFormWithErrors() {
        NewProductPayload payload = new NewProductPayload("   ", null);
        ConcurrentModel model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(productsRestClient)
                .createProduct("   ", null);

        String actual = controller.createProduct(payload, model);

        assertEquals("catalogue/products/new_product", actual);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));

        verify(productsRestClient).createProduct("   ", null);
        verifyNoMoreInteractions(productsRestClient);
    }


}