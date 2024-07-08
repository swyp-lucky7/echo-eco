package teamseven.echoeco.admin.background.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.admin.background.domain.Background;
import teamseven.echoeco.admin.background.repository.BackgroundRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackgroundService {
    private final BackgroundRepository backgroundRepository;

    public void save(Background background) {
        backgroundRepository.save(background);
    }

    public List<Background> findAll() {
        return backgroundRepository.findAll();
    }

    public Background findById(Long id) {
        return backgroundRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        backgroundRepository.deleteById(id);
    }
}
