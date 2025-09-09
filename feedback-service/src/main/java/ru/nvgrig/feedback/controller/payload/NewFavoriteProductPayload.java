package ru.nvgrig.feedback.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewFavoriteProductPayload(
        @NotNull(message = "{feedback.products.favorites.create.error.product_id_is_null}")
        Integer productId) {
}
