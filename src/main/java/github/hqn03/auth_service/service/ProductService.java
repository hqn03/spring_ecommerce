package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.product.ProductDetailResponse;
import github.hqn03.auth_service.dto.product.ProductRequest;
import github.hqn03.auth_service.dto.product.ProductResponse;
import github.hqn03.auth_service.dto.sku.SkuCreateRequest;
import github.hqn03.auth_service.dto.sku.SkuDetailResponse;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.ProductMapper;
import github.hqn03.auth_service.model.Category;
import github.hqn03.auth_service.model.Product;
import github.hqn03.auth_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SkuService skuService;
    private final CategoryService categoryService;

    @Transactional
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id " + id + " not found"));
    }

    @Transactional
    public ProductDetailResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);

        if (productRepository.existsBySlug(productRequest.slug())) {
            throw new AppException("Slug already existed", HttpStatus.BAD_REQUEST);
        }

        Category category = categoryService.findById(productRequest.categoryId());
        product.setCategory(category);

        Product saved = productRepository.save(product);

        return productMapper.toProductDetailResponse(saved);
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductDetailResponse getProductById(Long id) {
        Product product = this.findById(id);
        return productMapper.toProductDetailResponse(product);
    }

    public ProductDetailResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product slug is not found."));

        return productMapper.toProductDetailResponse(product);
    }

    @Transactional
    public ProductDetailResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = this.findById(id);

        productMapper.updateEntityFromRequest(productRequest, product);

        if (productRequest.categoryId() != null) {
            Category category = categoryService.findById(productRequest.categoryId());
            product.setCategory(category);
        }

        Product updated = productRepository.save(product);
        return productMapper.toProductDetailResponse(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = this.findById(id);
        productRepository.delete(product);
    }

    @Transactional
    public SkuDetailResponse addSku(Long productId, SkuCreateRequest request) {
        Product product = this.findById(productId);
        return skuService.createSku(product, request);
    }
}
