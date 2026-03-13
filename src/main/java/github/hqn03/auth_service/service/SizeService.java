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
public class SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    @Transactional(readOnly = true)
    public Size findById(Integer id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id " + id + " not found"));
    }

    @Transactional
    public SizeResponse createSize(SizeRequest request) {
        Size size = sizeMapper.toEntity(request);
        Size saved = sizeRepository.save(size);

        return sizeMapper.toSizeResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SizeResponse> getSizes() {
        return sizeRepository.findAll()
                .stream()
                .map(sizeMapper::toSizeResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SizeResponse getSizeById(int id) {
        Size size = this.findById(id);

        return sizeMapper.toSizeResponse(size);
    }

    @Transactional
    public SizeResponse updateSize(int id, SizeRequest request) {
        Size size = this.findById(id);

        sizeMapper.updateSize(request, size);
        Size updated = sizeRepository.save(size);

        return sizeMapper.toSizeResponse(updated);
    }

    @Transactional
    public void deleteSize(int id) {
        Size size = this.findById(id);

        sizeRepository.delete(size);
    }
}
