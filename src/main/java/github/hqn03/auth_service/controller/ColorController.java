package github.hqn03.auth_service.controller;

import github.hqn03.auth_service.dto.color.ColorRequest;
import github.hqn03.auth_service.dto.color.ColorResponse;
import github.hqn03.auth_service.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('COLOR:CREATE')")
    public ColorResponse createColor(@RequestBody ColorRequest request) {
        return colorService.createColor(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('COLOR:READ')")
    public List<ColorResponse> getColors() {
        return colorService.getColors();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('COLOR:READ')")
    public ColorResponse getColorById(@PathVariable int id) {
        return colorService.getColorById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('COLOR:UPDATE')")
    public ColorResponse updateColor(@PathVariable int id, @RequestBody ColorRequest request) {
        return colorService.updateColor(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('COLOR:DELETE')")
    public String deleteColor(@PathVariable int id) {
        colorService.deleteColor(id);
        return "Delete color successfully";
    }
}
