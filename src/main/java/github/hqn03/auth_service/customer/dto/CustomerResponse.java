package github.hqn03.auth_service.customer.dto;

import java.math.BigDecimal;

public record CustomerResponse(String fullName, String phone, String email, String address, BigDecimal totalSpent) {
}
