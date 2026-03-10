package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.category.CategoryDTO;
import github.hqn03.auth_service.dto.product.ProductDetailResponse;
import github.hqn03.auth_service.dto.product.ProductResponse;
import github.hqn03.auth_service.dto.product.ProductRequest;
import github.hqn03.auth_service.model.Category;
import github.hqn03.auth_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product);

    ProductResponse toProductResponse(Product product);

    ProductDetailResponse toProductDetailResponse(Product product);

    List<ProductResponse> toListResponses(List<Product> products);



    default CategoryDTO toCategoryDTO(Category category) {
        if(category == null) return null;
        return new CategoryDTO(category.getSlug(), category.getName());
    }
}
