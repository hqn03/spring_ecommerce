package github.hqn03.auth_service.product.dto;

import github.hqn03.auth_service.category.dto.CategoryDTO;

public record ProductResponse(Long id, String name, String slug, CategoryDTO category, String image) {
}
