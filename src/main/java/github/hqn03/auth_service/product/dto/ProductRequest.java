package github.hqn03.auth_service.product.dto;

import github.hqn03.auth_service.sku.dto.SkuCreateRequest;

import java.util.List;
import java.util.Set;

public record ProductRequest(
        String name,
        String slug,
        String description,
        Integer categoryId,
        List<String> generalImages,
        Set<SkuCreateRequest> skus) {
}
