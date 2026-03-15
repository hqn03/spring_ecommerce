package github.hqn03.auth_service.dto.product;

public record ProductUpdateRequest(
        String name,
        String slug,
        String description,
        Integer categoryId
) {
}
