package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.product.ProductDetailResponse;
import github.hqn03.auth_service.dto.product.ProductResponse;
import github.hqn03.auth_service.dto.product.ProductRequest;
import github.hqn03.auth_service.dto.product.ProductUpdateRequest;
import github.hqn03.auth_service.dto.sku.SkuCreateRequest;
import github.hqn03.auth_service.dto.sku.SkuDetailResponse;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.ProductMapper;
import github.hqn03.auth_service.mapper.SkuMapper;
import github.hqn03.auth_service.model.*;
import github.hqn03.auth_service.repository.CategoryRepository;
import github.hqn03.auth_service.repository.ColorRepository;
import github.hqn03.auth_service.repository.ProductRepository;
import github.hqn03.auth_service.repository.SizeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ImageService imageService;

    @Transactional
    public ProductDetailResponse createProduct(ProductRequest productRequest) {
        if (productRepository.existsBySlug(productRequest.slug())) {
            throw new AppException("Slug already existed", HttpStatus.BAD_REQUEST);
        }

        Product product = productMapper.toEntity(productRequest);
        product.setCategory(categoryRepository.getReferenceById(product.getCategory().getId()));
        product.getSkus().forEach(sku -> {
            sku.setColor(colorRepository.getReferenceById(sku.getColor().getId()));
            sku.setSize(sizeRepository.getReferenceById(sku.getSize().getId()));
        });

        Product saved = productRepository.save(product);

        imageService.createProductImages(saved.getId(), null, productRequest.generalImages());

        productRequest.skus().forEach(skuReq -> {
            saved.getSkus().stream()
                    .filter(s -> s.getSkuCode().equals(skuReq.skuCode()))
                    .findFirst()
                    .ifPresent(sku -> {
                        imageService.createProductImages(saved.getId(), sku.getId(), skuReq.images());
                    });
        });


        return productMapper.toProductDetailResponse(saved);
    }

    public List<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductDetailResponse getProductById(Long id) {
        Product product = productRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id " + id + " not found"));
        return productMapper.toProductDetailResponse(product);
    }

    public ProductDetailResponse getProductBySlug(String slug) {
        Product product = productRepository.findDetailBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product with slug '" + slug + "' not found."));
        return productMapper.toProductDetailResponse(product);
    }

    @Transactional
    public ProductDetailResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id " + id + " not found"));

        productMapper.updateEntityFromRequest(request, product);
        product.setCategory(categoryRepository.getReferenceById(request.categoryId()));

        Product updated = productRepository.save(product);
        return productMapper.toProductDetailResponse(updated);
    }

    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id " + id + " not found"));
        productRepository.delete(product);
    }

    @Transactional
    public SkuDetailResponse addSku(Long productId, SkuCreateRequest request){
        return skuService.createSku(productId, request);
    }
}
