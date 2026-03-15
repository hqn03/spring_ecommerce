package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.size.SizeRequest;
import github.hqn03.auth_service.dto.size.SizeResponse;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.SizeMapper;
import github.hqn03.auth_service.model.Size;
import github.hqn03.auth_service.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    @Transactional
    public SizeResponse createSize(SizeRequest request) {
        Size size = sizeMapper.toEntity(request);
        Size saved = sizeRepository.save(size);

        return sizeMapper.toSizeResponse(saved);
    }

    public List<SizeResponse> getSizes() {
        return sizeRepository.findAll()
                .stream()
                .map(sizeMapper::toSizeResponse)
                .toList();
    }

    public SizeResponse getSizeById(int id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id " + id + " not found"));

        return sizeMapper.toSizeResponse(size);
    }

    @Transactional
    public SizeResponse updateSize(int id, SizeRequest request) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id " + id + " not found"));

        sizeMapper.updateSize(request, size);
        Size updated = sizeRepository.save(size);

        return sizeMapper.toSizeResponse(updated);
    }

    @Transactional
    public void deleteSize(int id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id " + id + " not found"));

        sizeRepository.delete(size);
    }
}
