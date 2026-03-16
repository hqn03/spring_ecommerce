package github.hqn03.auth_service.dto.sku;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SkuCreateRequest(
        @NotNull Integer colorId,
        @NotNull Integer sizeId,
        @NotBlank String skuCode,
        @Min(0) BigDecimal price,
        @Min(0) Integer stockQty
) {
}
