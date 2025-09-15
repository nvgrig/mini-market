package ru.nvgrig.feedback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public Flux<FavoriteProduct> findFavoriteProducts(Mono<JwtAuthenticationToken> authenticationTokenMono) {
        return authenticationTokenMono.flatMapMany(token -> favoriteProductService.findFavoriteProducts(token.getToken().getSubject()));
    }

    @GetMapping("by-product-id/{productId}")
    public Mono<FavoriteProduct> findFavoriteProductByProductId(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @PathVariable("productId") int productId) {
        return authenticationTokenMono.flatMap(token -> favoriteProductService.findFavoriteProductByProduct(productId, token.getToken().getSubject()));
    }

    @PostMapping
    public Mono<ResponseEntity<FavoriteProduct>> addProductToFavorite(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @Valid @RequestBody Mono<NewFavoriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {
        return Mono.zip(authenticationTokenMono, payloadMono)
                .flatMap(tuple -> favoriteProductService
                        .addProductToFavorites(tuple.getT2().productId(), tuple.getT1().getToken().getSubject()))
                .map(favoriteProduct -> ResponseEntity.created(uriComponentsBuilder.replacePath("feedback-api/favorite-products/{productId}")
                                .build(favoriteProduct.getId()))
                        .body(favoriteProduct));
    }

    @DeleteMapping("by-product-id/{productId}")
    public Mono<ResponseEntity<Void>> removeProductFromFavorites(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @PathVariable("productId") int productId) {
        return authenticationTokenMono.flatMap(token -> favoriteProductService.removeProductFromFavorites(productId,
                        token.getToken().getSubject()))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
