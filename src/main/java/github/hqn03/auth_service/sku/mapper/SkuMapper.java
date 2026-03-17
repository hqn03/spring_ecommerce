package github.hqn03.auth_service.sku.mapper;

import github.hqn03.auth_service.product.entity.ProductImage;
import github.hqn03.auth_service.sku.dto.SkuCreateRequest;
import github.hqn03.auth_service.sku.dto.SkuDetailResponse;
import github.hqn03.auth_service.sku.dto.SkuUpdateRequest;
import github.hqn03.auth_service.sku.entity.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SkuMapper {

    @Mapping(target = "colorName", source = "color.name")
    @Mapping(target = "colorCode", source = "color.hexCode")
    @Mapping(target = "sizeName", source = "size.name")
    @Mapping(target = "skuImages", source = "images")
    SkuDetailResponse toSkuDetailResponse(Sku sku);

    default List<String> mapImages(Collection<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(ProductImage::getImageUrl)
                .toList();
    }

    @Mapping(target = "color.id", source = "colorId")
    @Mapping(target = "size.id", source = "sizeId")
    @Mapping(target = "images", ignore = true)
    Sku toEntity(SkuCreateRequest skuRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "size", ignore = true)
    void updateEntityFromRequest(SkuUpdateRequest request, @MappingTarget Sku sku);
}
