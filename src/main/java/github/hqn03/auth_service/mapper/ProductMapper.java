package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.category.CategoryDTO;
import github.hqn03.auth_service.dto.product.ProductDetailResponse;
import github.hqn03.auth_service.dto.product.ProductRequest;
import github.hqn03.auth_service.dto.product.ProductResponse;
import github.hqn03.auth_service.model.Category;
import github.hqn03.auth_service.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {SkuMapper.class})
public interface ProductMapper {

    @Mapping(target = "category.id", source = "categoryId")
    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product);

    ProductResponse toProductResponse(Product product);

    ProductDetailResponse toProductDetailResponse(Product product);

    @Named("toCategoryDTO")
    default CategoryDTO toCategoryDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(category.getId(), category.getSlug(), category.getName());
    }

    @AfterMapping
    default void linkSkus(@MappingTarget Product product) {
        if (product.getSkus() != null) {
            product.getSkus().forEach(sku -> sku.setProduct(product));
        }
    }
}
