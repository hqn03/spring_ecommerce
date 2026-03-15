package github.hqn03.auth_service.dto.cart;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        Long skuId,
        String productName,
        String productSlug,
        String colorName,
        String colorCode,
        String sizeName,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
