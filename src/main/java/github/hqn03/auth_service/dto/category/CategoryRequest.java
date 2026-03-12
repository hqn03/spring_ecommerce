package github.hqn03.auth_service.dto.category;

public record CategoryRequest(String name, String slug, String description, Integer parentId) {
}
