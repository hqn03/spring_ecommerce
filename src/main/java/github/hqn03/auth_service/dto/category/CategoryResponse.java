package github.hqn03.auth_service.dto.category;

public record CategoryResponse(Integer id, String name, String slug, String description, Integer parentId) {
}
