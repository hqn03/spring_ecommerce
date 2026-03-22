package github.hqn03.auth_service.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long skuId;
    private String name;
    private BigDecimal price;
    private String image;
    private Integer quantity;
}
