package github.hqn03.auth_service.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long skuId,
        String productName,
        String productSlug,
        String variant,
        Integer quantity,
        BigDecimal originalPrice,
        BigDecimal discountAmount,
        BigDecimal price,
        BigDecimal subTotal
) {
}
