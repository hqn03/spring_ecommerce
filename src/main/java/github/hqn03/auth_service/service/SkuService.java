package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.sku.SkuCreateRequest;
import github.hqn03.auth_service.dto.sku.SkuDetailResponse;
import github.hqn03.auth_service.dto.sku.SkuUpdateRequest;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.SkuMapper;
import github.hqn03.auth_service.model.Product;
import github.hqn03.auth_service.model.Sku;
import github.hqn03.auth_service.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkuService {
    private final SkuRepository skuRepository;
    private final SkuMapper skuMapper;

    @Transactional
    public SkuDetailResponse updateSku(Long id, SkuUpdateRequest request) {
        Sku sku = skuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sku id not found"));

        skuMapper.updateEntityFromRequest(request, sku);
        Sku updated = skuRepository.save(sku);

        return skuMapper.toSkuDetailResponse(updated);
    }

    @Transactional
    public SkuDetailResponse createVariant(Product product, SkuCreateRequest request) {
        if (skuRepository.existsBySkuCode(request.skuCode())) {
            throw new AppException("SKU code " + request.skuCode() + " is already in use.", HttpStatus.BAD_REQUEST);
        }

        Sku newSku = skuMapper.toEntity(request);
        newSku.setProduct(product);
        Sku created = skuRepository.save(newSku);
        return skuMapper.toSkuDetailResponse(created);
    }
}
