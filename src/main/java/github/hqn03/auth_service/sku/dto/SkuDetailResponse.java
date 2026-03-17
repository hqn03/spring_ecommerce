package github.hqn03.auth_service.sku.dto;

import java.math.BigDecimal;
import java.util.List;

public record SkuDetailResponse(
        Long id,
        String skuCode,
        BigDecimal price,
        Integer stockQty,
        String colorName,
        String colorCode,
        String sizeName,
        List<String> skuImages
) {

}
