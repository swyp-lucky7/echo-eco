package teamseven.echoeco.background.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.background.repository.BackgroundRepository;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.config.exception.NotAdminSettingException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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

    public Background findByLevelAndEnvironment(int level, Environment environment) throws NotAdminSettingException {
        Pageable pageable = PageRequest.of(0, 1);
        List<Background> backgrounds = backgroundRepository.findByLevelAndEnvironment(level, environment, pageable);
        if (backgrounds.isEmpty()) {
            throw new NotAdminSettingException("설정하지 않은 백그라운드 요청이 존재합니다.");
        }
        return backgrounds.get(0);
    }
}
