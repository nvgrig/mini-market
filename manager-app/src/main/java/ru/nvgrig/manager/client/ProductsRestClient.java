package ru.nvgrig.manager.client;

import ru.nvgrig.manager.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {
    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String details);

    Optional<Product> findProduct(int ptoductId);

    void updateProduct(int productId, String title, String details);

    void deleteProduct(int productId);
}
