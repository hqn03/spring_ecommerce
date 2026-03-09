package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.size.SizeRequest;
import github.hqn03.auth_service.dto.size.SizeResponse;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
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

    @Transactional
    public SizeResponse createSize(SizeRequest sizeRequest) {
        Size size = new Size();

        size.setName(sizeRequest.name());
        size.setDescription(sizeRequest.description());

        Size saved =  sizeRepository.save(size);

        return new SizeResponse(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Transactional(readOnly = true)
    public List<SizeResponse> getSizes(){
        return sizeRepository.findAll()
                .stream()
                .map(size -> new SizeResponse(size.getId(), size.getName(), size.getDescription()))
                .toList();
    }

    @Transactional(readOnly = true)
    public SizeResponse getSizeById(int id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id not found"));

        return new SizeResponse(size.getId(), size.getName(), size.getDescription());
    }

    @Transactional
    public SizeResponse updateSize(int id,SizeRequest sizeRequest) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id not found"));

        size.setName(sizeRequest.name());
        size.setDescription(sizeRequest.description());

        Size updated = sizeRepository.save(size);
        return new SizeResponse(updated.getId(), updated.getName(), updated.getDescription());
    }

    @Transactional
    public void deleteSize(int id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size id not found"));

        sizeRepository.delete(size);
    }
}
