package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.color.ColorRequest;
import github.hqn03.auth_service.dto.color.ColorResponse;
import github.hqn03.auth_service.model.Color;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    Color toEntity(ColorRequest request);

    ColorResponse toColorResponse(Color color);

    void updateColorFromRequest(ColorRequest request, @MappingTarget Color color);
}
