package github.hqn03.auth_service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String skuCode;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String variant;
    private String image;
}
