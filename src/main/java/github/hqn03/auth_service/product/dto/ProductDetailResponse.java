package github.hqn03.auth_service.product.dto;

import github.hqn03.auth_service.category.dto.CategoryDTO;
import github.hqn03.auth_service.sku.dto.SkuDetailResponse;

import java.util.List;

public record ProductDetailResponse(
        Long id,
        String name,
        String slug,
        String description,
        CategoryDTO category,
        List<String> generalImages,
        List<SkuDetailResponse> skus) {
}
