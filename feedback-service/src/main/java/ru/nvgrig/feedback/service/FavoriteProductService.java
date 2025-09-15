package ru.nvgrig.feedback.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nvgrig.feedback.entity.FavoriteProduct;

public interface FavoriteProductService {

    Mono<FavoriteProduct> addProductToFavorites(int productId, String userId);

    Mono<Void> removeProductFromFavorites(int productId, String userId);

    Mono<FavoriteProduct> findFavoriteProductByProduct(int productId, String userId);

    Flux<FavoriteProduct> findFavoriteProducts(String userId);
}
