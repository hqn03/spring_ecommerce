package github.hqn03.auth_service.controller;

import github.hqn03.auth_service.dto.category.CategoryRequest;
import github.hqn03.auth_service.dto.category.CategoryResponse;
import github.hqn03.auth_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CATEGORY:CREATE')")
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CATEGORY:READ')")
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CATEGORY:READ')")
    public CategoryResponse getCategoryById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CATEGORY:READ')")
    public CategoryResponse updateCategory(@PathVariable int id, @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CATEGORY:READ')")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return "Delete size successfully";
    }
}
