package github.hqn03.auth_service.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemAddRequest(
        @NotNull(message = "Sku id not null")
        Long skuId,

        @NotNull(message = "quantity not nul")
        @Min(value = 1, message = "Quantity is at least 1")
        Integer quantity
) {
}
