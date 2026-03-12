package github.hqn03.auth_service.controller;

import github.hqn03.auth_service.dto.product.ProductDetailResponse;
import github.hqn03.auth_service.dto.product.ProductRequest;
import github.hqn03.auth_service.dto.product.ProductResponse;
import github.hqn03.auth_service.dto.sku.SkuCreateRequest;
import github.hqn03.auth_service.dto.sku.SkuDetailResponse;
import github.hqn03.auth_service.service.ProductService;
import lombok.RequiredArgsConstructor;
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
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:READ')")
    public ProductDetailResponse getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/s/{slug}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:READ')")
    public ProductDetailResponse getProductBySlug(@PathVariable String slug){
        return productService.getProductBySlug(slug);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:UPDATE')")
    public ProductDetailResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest){
        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:DELETE')")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "Delete product successfully";
    }

    @PostMapping("/{id}/sku")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PRODUCT:UPDATE')")
    public SkuDetailResponse addSku(@PathVariable Long id, @RequestBody SkuCreateRequest request){
        return productService.addSku(id, request);
    }
}
