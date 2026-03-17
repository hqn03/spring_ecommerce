package github.hqn03.auth_service.attribute.controller;

import github.hqn03.auth_service.attribute.dto.size.SizeRequest;
import github.hqn03.auth_service.attribute.dto.size.SizeResponse;
import github.hqn03.auth_service.attribute.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService sizeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SIZE:CREATE')")
    public SizeResponse createSize(@RequestBody SizeRequest request) {
        return sizeService.createSize(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SIZE:READ')")
    public List<SizeResponse> getSizes() {
        return sizeService.getSizes();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SIZE:READ')")
    public SizeResponse getSizeById(@PathVariable int id) {
        return sizeService.getSizeById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SIZE:READ')")
    public SizeResponse updateSize(@PathVariable int id, @RequestBody SizeRequest request) {
        return sizeService.updateSize(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SIZE:READ')")
    public String deleteSize(@PathVariable int id) {
        sizeService.deleteSize(id);
        return "Delete size successfully";
    }


}
