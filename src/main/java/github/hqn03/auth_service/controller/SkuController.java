package github.hqn03.auth_service.controller;

import github.hqn03.auth_service.dto.sku.SkuDetailResponse;
import github.hqn03.auth_service.dto.sku.SkuUpdateRequest;
import github.hqn03.auth_service.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skus")
@RequiredArgsConstructor
public class SkuController {
    private final SkuService skuService;

    @PatchMapping("/{id}")
    public SkuDetailResponse updateSku(@PathVariable Long id, @RequestBody SkuUpdateRequest request) {
        return skuService.updateSku(id, request);
    }

    @GetMapping
    public List<SkuDetailResponse> getSkus() {
        return skuService.getSkus();
    }

    @DeleteMapping("/{id}")
    public String deleteSku(@PathVariable Long id) {
         skuService.deleteSku(id);
         return "Delete sku successfully";
    }

}
