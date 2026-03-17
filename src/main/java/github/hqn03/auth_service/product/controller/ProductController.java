package github.hqn03.auth_service.product.controller;

import github.hqn03.auth_service.product.dto.ProductDetailResponse;
import github.hqn03.auth_service.product.dto.ProductRequest;
import github.hqn03.auth_service.product.dto.ProductResponse;
import github.hqn03.auth_service.product.dto.ProductUpdateRequest;
import github.hqn03.auth_service.product.service.ProductService;
import github.hqn03.auth_service.sku.dto.SkuCreateRequest;
import github.hqn03.auth_service.sku.dto.SkuDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PRODUCT:CREATE')")
    public ProductDetailResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:READ')")
    public List<ProductResponse> getProducts(@ParameterObject Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:READ')")
    public ProductDetailResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/s/{slug}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:READ')")
    public ProductDetailResponse getProductBySlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:UPDATE')")
    public ProductDetailResponse updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:DELETE')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Delete product successfully";
    }

    @PostMapping("/{id}/sku")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:UPDATE')")
    public SkuDetailResponse addSku(@PathVariable Long id, @RequestBody SkuCreateRequest request) {
        return productService.addSku(id, request);
    }
}
