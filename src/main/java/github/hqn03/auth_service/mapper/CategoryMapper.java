package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.category.CategoryDTO;
import github.hqn03.auth_service.dto.category.CategoryRequest;
import github.hqn03.auth_service.dto.category.CategoryResponse;
import github.hqn03.auth_service.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    @Mapping(target = "parent", source = "parent")
    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(CategoryRequest request, @MappingTarget Category category);

    default CategoryDTO toCategoryDTO(Category category){
        if (category == null) return null;
        return new CategoryDTO(category.getId(), category.getSlug(), category.getName());
    }
}
