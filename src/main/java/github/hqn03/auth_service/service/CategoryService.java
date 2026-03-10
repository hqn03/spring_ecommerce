package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.category.CategoryRequest;
import github.hqn03.auth_service.dto.category.CategoryResponse;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.model.Category;
import github.hqn03.auth_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        category.setParentId(categoryRequest.parentId());

        categoryRepository.save(category);

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(saved.getId(), saved.getName(), saved.getSlug(), saved.getDescription(), saved.getParentId());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName(), category.getSlug(), category.getDescription(), category.getParentId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id not found"));

        return new CategoryResponse(category.getId(), category.getName(), category.getSlug(), category.getDescription(), category.getParentId());
    }

    @Transactional
    public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id not found"));

        category.setName(categoryRequest.name());
        category.setSlug(categoryRequest.slug());
        category.setDescription(categoryRequest.description());
        category.setParentId(categoryRequest.parentId());

        Category updated = categoryRepository.save(category);
        return new CategoryResponse(updated.getId(), updated.getName(), updated.getSlug(), updated.getDescription(), updated.getParentId());
    }

    @Transactional
    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id not found"));

        categoryRepository.delete(category);
    }
}
