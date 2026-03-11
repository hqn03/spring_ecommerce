package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.size.SizeRequest;
import github.hqn03.auth_service.dto.size.SizeResponse;
import github.hqn03.auth_service.model.Size;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface SizeMapper {

    Size toEntity(SizeRequest request);

    SizeResponse toSizeResponse(Size size);

    void updateSize(SizeRequest request, @MappingTarget Size size);
}
