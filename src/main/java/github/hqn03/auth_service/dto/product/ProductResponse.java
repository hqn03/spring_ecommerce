package github.hqn03.auth_service.dto.product;

import github.hqn03.auth_service.dto.category.CategoryDTO;

public record ProductResponse(Long id, String name, String slug, CategoryDTO category) {
}
