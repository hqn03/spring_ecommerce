package github.hqn03.auth_service.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        List<CartItemDto> items,
        BigDecimal totalPrice,
        Integer totalItems
) {
}
