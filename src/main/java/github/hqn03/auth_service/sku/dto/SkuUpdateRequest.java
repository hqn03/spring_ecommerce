package github.hqn03.auth_service.sku.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record SkuUpdateRequest(
        @NotBlank String skuCode,
        @Min(0) BigDecimal price,
        @Min(0) Integer stockQty
) {
}
