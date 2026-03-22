package github.hqn03.auth_service.order.dto;

import java.util.List;

public record OrderCreateRequest(List<Long> cartItemIds) {
}
