package ru.nvgrig.feedback.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nvgrig.feedback.entity.FavoriteProduct;

public interface FavoriteProductService {

    Mono<FavoriteProduct> addProductToFavorites(int productId);

    Mono<Void> removeProductFromFavorites(int productId);

    Mono<FavoriteProduct> findFavoriteProductByProduct(int productId);

    Flux<FavoriteProduct> findFavoriteProducts();
}
