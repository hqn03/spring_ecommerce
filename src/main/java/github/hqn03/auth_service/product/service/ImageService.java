package github.hqn03.auth_service.product.service;

import github.hqn03.auth_service.product.entity.Product;
import github.hqn03.auth_service.product.entity.ProductImage;
import github.hqn03.auth_service.product.repository.ProductImageRepository;
import github.hqn03.auth_service.product.repository.ProductRepository;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;


    @Transactional
    public List<ProductImage> createProductImages(Long productId, Long skuId, List<String> imageUrls){
        if (imageUrls == null || imageUrls.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductImage> images = new ArrayList<>();
        Product product = productRepository.getReferenceById(productId);
        Sku sku = (skuId != null) ? skuRepository.getReferenceById(skuId) : null;

        for (int i = 0; i < imageUrls.size(); i++) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setSku(sku);
            productImage.setImageUrl(imageUrls.get(i));
            productImage.setIsMain(i == 0);
            productImage.setSortOrder(i);
            images.add(productImage);
        }

        return productImageRepository.saveAll(images);
    }
}
