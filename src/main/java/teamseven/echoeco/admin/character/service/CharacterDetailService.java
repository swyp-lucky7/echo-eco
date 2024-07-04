package teamseven.echoeco.admin.character.service;

import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.admin.character.domain.CharacterDetail;
import teamseven.echoeco.admin.character.repository.CharacterDetailRepository;

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
}
