package ru.nvgrig.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nvgrig.customer.entity.FavoriteProduct;
import ru.nvgrig.customer.repository.FavoriteProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultFavoriteProductService implements FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;

    @Override
    public Mono<FavoriteProduct> addProductToFavorites(int productId) {
        return favoriteProductRepository.save(new FavoriteProduct(UUID.randomUUID(), productId));
    }

    @Override
    public Mono<Void> removeProductFromFavorites(int productId) {
        return favoriteProductRepository.deleteByProductId(productId);
    }

    @Override
    public Mono<FavoriteProduct> findFavoriteProductByProduct(int productId) {
        return favoriteProductRepository.findByProductId(productId);
    }

    @Override
    public Flux<FavoriteProduct> findFavoriteProducts() {
        return favoriteProductRepository.findAll();
    }
}
