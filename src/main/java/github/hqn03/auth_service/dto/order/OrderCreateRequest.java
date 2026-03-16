package github.hqn03.auth_service.dto.order;

import java.util.List;

public record OrderCreateRequest(
        String shippingFullName,
        String shippingPhone,
        String shippingAddress,

        List<Long> cartItemIds
) {
}
