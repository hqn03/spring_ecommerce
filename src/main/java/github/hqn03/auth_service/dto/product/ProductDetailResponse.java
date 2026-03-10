package github.hqn03.auth_service.dto.product;

import github.hqn03.auth_service.dto.category.CategoryDTO;

public record ProductDetailResponse(Long id, String name, String slug, String description, CategoryDTO category) {
}
