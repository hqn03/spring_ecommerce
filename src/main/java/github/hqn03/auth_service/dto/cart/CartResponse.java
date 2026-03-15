package github.hqn03.auth_service.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        List<ItemResponse> items,
        BigDecimal totalPrice,
        Integer totalItems
) {
}
