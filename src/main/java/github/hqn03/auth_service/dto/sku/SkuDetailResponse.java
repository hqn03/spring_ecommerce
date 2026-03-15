package github.hqn03.auth_service.dto.sku;

import java.math.BigDecimal;

public record SkuDetailResponse(
        Long id,
        String skuCode,
        BigDecimal price,
        Integer stockQty,
        String colorName,
        String colorCode,
        String sizeName
) {

}
