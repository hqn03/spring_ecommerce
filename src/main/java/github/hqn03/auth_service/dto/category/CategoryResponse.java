package github.hqn03.auth_service.dto.category;

public record CategoryResponse(int id, String name, String slug, String description, int parentId) {
}
