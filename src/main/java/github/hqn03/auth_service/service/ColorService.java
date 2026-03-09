package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.color.ColorRequest;
import github.hqn03.auth_service.dto.color.ColorResponse;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.model.Color;
import github.hqn03.auth_service.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;

    @Transactional
    public ColorResponse createColor(ColorRequest colorRequest) {
        Color color = new Color();

        color.setName(colorRequest.name());
        color.setHexCode(colorRequest.hexCode());

        Color saved = colorRepository.save(color);
        return new ColorResponse(saved.getId(), saved.getName(), saved.getHexCode());
    }

    @Transactional(readOnly = true)
    public List<ColorResponse> getColors() {
        return colorRepository.findAll()
                .stream()
                .map(color -> new ColorResponse(color.getId(), color.getName(), color.getHexCode())).toList();
    }

    @Transactional(readOnly = true)
    public ColorResponse getColorById(int id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id not found"));

        return new ColorResponse(color.getId(), color.getName(), color.getHexCode());
    }

    @Transactional
    public ColorResponse updateColor(int id,  ColorRequest colorRequest) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id not found"));

        color.setName(colorRequest.name());
        color.setHexCode(colorRequest.hexCode());

        Color saved = colorRepository.save(color);
        return new ColorResponse(saved.getId(), saved.getName(), saved.getHexCode());
    }

    @Transactional
    public void deleteColor(int id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id not found"));

        colorRepository.delete(color);
    }
}
