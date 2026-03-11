package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.color.ColorRequest;
import github.hqn03.auth_service.dto.color.ColorResponse;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.ColorMapper;
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
    private final ColorMapper colorMapper;

    @Transactional(readOnly = true)
    public Color findById(Integer id){
        return colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color id " + id + " not found"));
    }

    @Transactional
    public ColorResponse createColor(ColorRequest request) {
        Color color = colorMapper.toEntity(request);
        Color saved = colorRepository.save(color);
        return colorMapper.toColorResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ColorResponse> getColors() {
        return colorRepository.findAll()
                .stream()
                .map(colorMapper::toColorResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ColorResponse getColorById(Integer id) {
        Color color = this.findById(id);
        return colorMapper.toColorResponse(color);
    }

    @Transactional
    public ColorResponse updateColor(Integer id,  ColorRequest request) {
        Color color = this.findById(id);

        colorMapper.updateColorFromRequest(request, color);
        Color updated = colorRepository.save(color);
        return colorMapper.toColorResponse(updated);
    }

    @Transactional
    public void deleteColor(Integer id) {
        Color color = this.findById(id);
        colorRepository.delete(color);
    }
}
