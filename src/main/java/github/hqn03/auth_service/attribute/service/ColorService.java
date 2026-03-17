package github.hqn03.auth_service.attribute.service;

import github.hqn03.auth_service.attribute.dto.color.ColorRequest;
import github.hqn03.auth_service.attribute.dto.color.ColorResponse;
import github.hqn03.auth_service.attribute.entity.Color;
import github.hqn03.auth_service.attribute.mapper.ColorMapper;
import github.hqn03.auth_service.attribute.repository.ColorRepository;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    @Transactional
    public ColorResponse createColor(ColorRequest request) {
        Color color = colorMapper.toEntity(request);
        Color saved = colorRepository.save(color);
        return colorMapper.toColorResponse(saved);
    }

    public List<ColorResponse> getColors() {
        return colorRepository.findAll()
                .stream()
                .map(colorMapper::toColorResponse)
                .toList();
    }

    public ColorResponse getColorById(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id " + id + " not found"));
        return colorMapper.toColorResponse(color);
    }

    @Transactional
    public ColorResponse updateColor(Integer id, ColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id " + id + " not found"));

        colorMapper.updateColorFromRequest(request, color);
        Color updated = colorRepository.save(color);
        return colorMapper.toColorResponse(updated);
    }

    @Transactional
    public void deleteColor(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id " + id + " not found"));
        colorRepository.delete(color);
    }
}
