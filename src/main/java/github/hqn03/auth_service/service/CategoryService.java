package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.category.CategoryRequest;
import github.hqn03.auth_service.dto.category.CategoryResponse;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.CategoryMapper;
import github.hqn03.auth_service.model.Category;
import github.hqn03.auth_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id " + id + " is not found."));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);

        if (request.parentId() != null) {
            Category parentCategory = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category id not found: " + request.parentId()));
            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }

        Category saved = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(saved);
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = this.findById(id);

        return categoryMapper.toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category category = this.findById(id);

        categoryMapper.updateCategory(request, category);

        Integer currentParentId = (category.getParent() != null) ? category.getParent().getId() : null;
        Integer newParentId = request.parentId();

        if(!Objects.equals(currentParentId, newParentId)) {
            if(newParentId != null){
                if (id.equals(newParentId)) {
                    throw new AppException("A category cannot be its own parent category.", HttpStatus.BAD_REQUEST);
                }

                Category newParent = this.findById(newParentId);

                if (this.isChildOf(newParent, category)){
                    throw new AppException("Cannot select child category as parent category.",  HttpStatus.BAD_REQUEST);
                }

                category.setParent(newParent);
            }
            else {
                category.setParent(null);
            }
        }

        Category updated = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(updated);
    }

    @Transactional
    public void deleteCategory(int id) {
        Category category = this.findById(id);

        categoryRepository.delete(category);
    }

    private boolean isChildOf(Category potentialParent, Category currentCategory) {
        Category temp = potentialParent.getParent();
        while (temp != null) {
            if(temp.getId().equals(currentCategory.getId())){
                return true;
            }
            temp = temp.getParent();
        }
        return false;
    }
}
