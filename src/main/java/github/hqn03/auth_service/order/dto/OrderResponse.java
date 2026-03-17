package github.hqn03.auth_service.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        LocalDateTime orderDate,
        String shippingFullName,
        String shippingPhone,
        String shippingAddress,
        BigDecimal totalAmount,
        String status,
        List<OrderItemResponse> items
) {
}
