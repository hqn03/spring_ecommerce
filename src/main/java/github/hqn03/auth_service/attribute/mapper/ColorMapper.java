package github.hqn03.auth_service.attribute.mapper;

import github.hqn03.auth_service.attribute.dto.color.ColorRequest;
import github.hqn03.auth_service.attribute.dto.color.ColorResponse;
import github.hqn03.auth_service.attribute.entity.Color;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    Color toEntity(ColorRequest request);

    ColorResponse toColorResponse(Color color);

    void updateColorFromRequest(ColorRequest request, @MappingTarget Color color);
}
