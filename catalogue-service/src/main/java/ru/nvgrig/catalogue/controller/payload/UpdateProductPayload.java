package ru.nvgrig.catalogue.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductPayload(
        @NotNull(message = "{catalogue.products.edit.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{catalogue.products.edit.errors.title_size_is_invalid}")
        String title,
        @Size(max = 1000, message = "{catalogue.products.edit.errors.details_size_is_invalid}")
        String details) {
}
