package github.hqn03.auth_service.category.dto;

public record CategoryRequest(String name, String slug, String description, Integer parentId) {
}
