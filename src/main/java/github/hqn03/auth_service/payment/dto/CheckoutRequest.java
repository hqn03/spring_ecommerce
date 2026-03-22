package github.hqn03.auth_service.payment.dto;

import java.util.List;

public record CheckoutRequest(
        List<Long> skuIds
) {
}
