package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.category.CategoryRequest;
import github.hqn03.auth_service.dto.category.CategoryResponse;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();

        category.setName(categoryRequest.name());
        category.setSlug(categoryRequest.slug());
        category.setDescription(categoryRequest.description());

        if (categoryRequest.parentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryRequest.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category id not found: " + categoryRequest.parentId()));
            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }

        Category saved = categoryRepository.save(category);
        Integer parentId = saved.getParent() != null ? saved.getParent().getId() : null;

        return new CategoryResponse(saved.getId(), saved.getName(), saved.getSlug(), saved.getDescription(), parentId);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> {
                    Integer parentId = category.getParent() != null ? category.getParent().getId() : null;
                    return new CategoryResponse(category.getId(), category.getName(), category.getSlug(), category.getDescription(), parentId);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id not found"));
        Integer parentId = category.getParent() != null ? category.getParent().getId() : null;

        return new CategoryResponse(category.getId(), category.getName(), category.getSlug(), category.getDescription(), parentId);
    }

    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id not found"));

        category.setName(categoryRequest.name());
        category.setSlug(categoryRequest.slug());
        category.setDescription(categoryRequest.description());

        Integer currentParentId = (category.getParent() != null) ? category.getParent().getId() : null;
        Integer newParentId = categoryRequest.parentId();

        if(!Objects.equals(currentParentId, newParentId)) {
            if(newParentId != null){
                if (id.equals(newParentId)) {
                    throw new AppException("A category cannot be its own parent category.", HttpStatus.BAD_REQUEST);
                }

                Category newParent = categoryRepository.findById(newParentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Parent category id not found: " + categoryRequest.parentId()));

                if (isChildOf(newParent, category)){
                    throw new AppException("Cannot select child category as parent category.",  HttpStatus.BAD_REQUEST);
                }

                category.setParent(newParent);
            }
            else {
                category.setParent(null);
            }
        }

        Category updated = categoryRepository.save(category);
        Integer parentId = updated.getParent() != null ? updated.getParent().getId() : null;

        return new CategoryResponse(updated.getId(), updated.getName(), updated.getSlug(), updated.getDescription(), parentId);
    }

    @Transactional
    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id not found"));

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
