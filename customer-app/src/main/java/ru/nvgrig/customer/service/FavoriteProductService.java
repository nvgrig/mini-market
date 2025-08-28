package ru.nvgrig.customer.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nvgrig.customer.entity.FavoriteProduct;

public interface FavoriteProductService {

    Mono<FavoriteProduct> addProductToFavorites(int productId);

    Mono<Void> removeProductFromFavorites(int productId);

    Mono<FavoriteProduct> findFavoriteProductByProduct(int productId);

    Flux<FavoriteProduct> findFavoriteProducts();
}
