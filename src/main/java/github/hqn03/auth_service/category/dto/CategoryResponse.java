package github.hqn03.auth_service.category.dto;

public record CategoryResponse(Integer id, String name, String slug, String description, CategoryDTO parent) {
}
