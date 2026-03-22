package github.hqn03.auth_service.payment.dto;

import com.stripe.model.Address;

public record StripeData(
        String email,
        String address,
        String phone,
        String fullName,
        String orderKey,
        String username,
        String currency,
        String method,
        String cartKey
) {
}
