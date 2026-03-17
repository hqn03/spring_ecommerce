package github.hqn03.auth_service.attribute.mapper;

import github.hqn03.auth_service.attribute.dto.size.SizeRequest;
import github.hqn03.auth_service.attribute.dto.size.SizeResponse;
import github.hqn03.auth_service.attribute.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SizeMapper {

    Size toEntity(SizeRequest request);

    SizeResponse toSizeResponse(Size size);

    void updateSize(SizeRequest request, @MappingTarget Size size);
}
