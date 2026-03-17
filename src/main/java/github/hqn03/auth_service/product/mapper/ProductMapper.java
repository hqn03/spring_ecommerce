package github.hqn03.auth_service.product.mapper;

import github.hqn03.auth_service.product.dto.ProductDetailResponse;
import github.hqn03.auth_service.product.dto.ProductRequest;
import github.hqn03.auth_service.product.dto.ProductResponse;
import github.hqn03.auth_service.product.dto.ProductUpdateRequest;
import github.hqn03.auth_service.product.entity.Product;
import github.hqn03.auth_service.product.entity.ProductImage;
import github.hqn03.auth_service.sku.mapper.SkuMapper;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SkuMapper.class})
public interface ProductMapper {

    @Mapping(target = "category.id", source = "categoryId")
    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

    @Mapping(target = "image", source = "images", qualifiedByName = "getMainImage")
    ProductResponse toProductResponse(Product product);

    @Named("getMainImage")
    default String getMainImage(Collection<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return "https://picsum.photos/800/1000"; // Ảnh mặc định nếu không có ảnh nào
        }
        return images.stream()
                .filter(ProductImage::getIsMain) // Lấy ảnh có is_main = true
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(images.iterator().next().getImageUrl()); // Nếu không có is_main, lấy tấm đầu tiên
    }

    @Mapping(target = "generalImages", source = "images", qualifiedByName = "filterGeneralImages")
    ProductDetailResponse toProductDetailResponse(Product product);

    @Named("filterGeneralImages")
    default List<String> filterGeneralImages(Collection<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .filter(img -> img.getSku() == null) // Chỉ lấy ảnh chung
                .map(ProductImage::getImageUrl)
                .toList();
    }

    @AfterMapping
    default void linkSkus(ProductRequest productRequest, @MappingTarget Product product) {
        if (product.getSkus() == null) return;

        product.getSkus().forEach(sku -> sku.setProduct(product));
    }
}
