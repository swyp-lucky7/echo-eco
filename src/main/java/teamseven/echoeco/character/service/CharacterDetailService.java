package teamseven.echoeco.character.service;

import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamseven.echoeco.character.domain.CharacterDetail;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.character.repository.CharacterDetailRepository;
import teamseven.echoeco.config.exception.NotAdminSettingException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterDetailService {
    private final CharacterDetailRepository characterDetailRepository;

    public void save(CharacterDetail characterDetail) {
        characterDetailRepository.save(characterDetail);
    }

    public List<CharacterDetail> findByCharacterId(Long characterId) {
        return characterDetailRepository.findByCharacter_IdOrderByLevelAsc(characterId);
    }

    public CharacterDetail findById(Long id) {
        return characterDetailRepository.findById(id).orElseThrow(() -> new NotFoundException("Character Detail Not Found"));
    }

    public void delete(Long id) {
        characterDetailRepository.deleteById(id);
    }

    public CharacterDetail findByLevelAndEnvironment(int level, Environment environment) throws NotAdminSettingException {
        Pageable pageable = PageRequest.of(0, 1);
        List<CharacterDetail> characterDetails = characterDetailRepository.findByLevelAndEnvironment(level, environment, pageable);
        if (characterDetails.isEmpty()) {
            throw new NotAdminSettingException("설정하지 않은 캐릭터 디테일 요청이 존재합니다.");
        }
        return characterDetails.get(0);
    }
}
