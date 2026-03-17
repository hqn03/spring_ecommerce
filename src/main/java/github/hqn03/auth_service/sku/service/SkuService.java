package github.hqn03.auth_service.sku.service;

import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.product.entity.Product;
import github.hqn03.auth_service.product.repository.ProductRepository;
import github.hqn03.auth_service.sku.dto.SkuCreateRequest;
import github.hqn03.auth_service.sku.dto.SkuDetailResponse;
import github.hqn03.auth_service.sku.dto.SkuUpdateRequest;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.mapper.SkuMapper;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuService {
    private final SkuRepository skuRepository;
    private final SkuMapper skuMapper;
    private final ProductRepository productRepository;

    @Transactional
    public SkuDetailResponse updateSku(Long id, SkuUpdateRequest request) {
        Sku sku = skuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sku id not found"));

        skuMapper.updateEntityFromRequest(request, sku);
        Sku updated = skuRepository.save(sku);

        return skuMapper.toSkuDetailResponse(updated);
    }


    @Transactional
    public SkuDetailResponse createSku(Long productId, SkuCreateRequest request) {
        Product product = productRepository.getReferenceById(productId);

        if (skuRepository.existsBySkuCode(request.skuCode())) {
            throw new AppException("SKU code " + request.skuCode() + " is already in use.", HttpStatus.BAD_REQUEST);
        }

        Sku newSku = skuMapper.toEntity(request);
        newSku.setProduct(product);
        Sku created = skuRepository.save(newSku);
        return skuMapper.toSkuDetailResponse(created);
    }

    public List<SkuDetailResponse> getSkus() {
        return skuRepository.findAll()
                .stream()
                .map(skuMapper::toSkuDetailResponse)
                .toList();
    }

    @Transactional
    public void deleteSku(Long id) {
        Sku sku = skuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sku id not found"));

        skuRepository.delete(sku);
    }
}
