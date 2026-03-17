package github.hqn03.auth_service.product.dto;

public record ProductUpdateRequest(
        String name,
        String slug,
        String description,
        Integer categoryId
) {
}
