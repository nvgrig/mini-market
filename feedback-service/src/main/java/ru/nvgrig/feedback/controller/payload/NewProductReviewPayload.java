package ru.nvgrig.feedback.controller.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductReviewPayload(
        @NotNull(message = "{feedback.products.reviews.create.error.product_id_is_null}")
        int productId,
        @NotNull(message = "{feedback.products.reviews.create.error.rating_is_null}")
        @Min(value = 1, message = "{feedback.products.reviews.create.error.rating_is_below_min}")
        @Max(value = 5, message = "{feedback.products.reviews.create.error.rating_is_above_max}")
        Integer rating,
        @Size(max = 1000, message = "{feedback.products.reviews.create.error.review_is_too_big}")
        String review) {
}
