package teamseven.echoeco.admin.character.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.admin.character.domain.CharacterDetail;
import teamseven.echoeco.admin.character.repository.CharacterDetailRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterDetailService {
    private final CharacterDetailRepository characterDetailRepository;

    public void save(CharacterDetail characterDetail) {
        characterDetailRepository.save(characterDetail);
    }
}
