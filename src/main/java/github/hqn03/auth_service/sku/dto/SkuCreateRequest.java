package github.hqn03.auth_service.sku.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record SkuCreateRequest(
        @NotNull Integer colorId,
        @NotNull Integer sizeId,
        @NotBlank String code,
        @Min(0) BigDecimal price,
        @Min(0) Integer stockQty,
        List<String> images
) {
}
