package github.hqn03.auth_service.dto.product;

import github.hqn03.auth_service.dto.sku.SkuCreateRequest;

import java.util.Set;

public record ProductRequest(
        String name,
        String slug,
        String description,
        Integer categoryId,
        Set<SkuCreateRequest> skus) {

}
