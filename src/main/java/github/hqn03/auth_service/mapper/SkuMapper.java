package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.sku.SkuCreateRequest;
import github.hqn03.auth_service.dto.sku.SkuDetailResponse;
import github.hqn03.auth_service.dto.sku.SkuUpdateRequest;
import github.hqn03.auth_service.model.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SkuMapper {

    @Mapping(target = "colorName", source = "color.name")
    @Mapping(target = "colorCode", source = "color.hexCode")
    @Mapping(target = "sizeName", source = "size.name")
    SkuDetailResponse toSkuDetailResponse(Sku sku);

    @Mapping(target = "color.id", source = "colorId")
    @Mapping(target = "size.id", source = "sizeId")
    Sku toEntity(SkuCreateRequest skuRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "size", ignore = true)
    void updateEntityFromRequest(SkuUpdateRequest request, @MappingTarget Sku sku);
}
