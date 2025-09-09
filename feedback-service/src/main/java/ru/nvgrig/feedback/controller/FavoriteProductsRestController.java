package ru.nvgrig.feedback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nvgrig.feedback.controller.payload.NewFavoriteProductPayload;
import ru.nvgrig.feedback.entity.FavoriteProduct;
import ru.nvgrig.feedback.service.FavoriteProductService;

@RestController
@RequestMapping("feedback-api/favorite-products")
@RequiredArgsConstructor
public class FavoriteProductsRestController {

    private final FavoriteProductService favoriteProductService;

    @GetMapping
    public Flux<FavoriteProduct> findFavoriteProducts() {
        return favoriteProductService.findFavoriteProducts();
    }

    @GetMapping("by-product-id/{productId}")
    public Mono<FavoriteProduct> findFavoriteProductByProductId(@PathVariable("productId") int productId) {
        return favoriteProductService.findFavoriteProductByProduct(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<FavoriteProduct>> addProductToFavorite(@Valid @RequestBody Mono<NewFavoriteProductPayload> payloadMono,
                                                                      UriComponentsBuilder uriComponentsBuilder) {
        return payloadMono
                .flatMap(payload -> favoriteProductService.addProductToFavorites(payload.productId()))
                .map(favoriteProduct -> ResponseEntity.created(uriComponentsBuilder.replacePath("feedback-api/favorite-products/{productId}")
                                .build(favoriteProduct.getId()))
                        .body(favoriteProduct));
    }

    @DeleteMapping("by-product-id/{productId}")
    public Mono<ResponseEntity<Void>> removeProductFromFavorites(@PathVariable("productId") int productId) {
        return favoriteProductService.removeProductFromFavorites(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
