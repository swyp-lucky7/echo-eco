package teamseven.echoeco.admin.character.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterType;
import teamseven.echoeco.admin.character.domain.dto.CharacterPickListCondition;
import teamseven.echoeco.admin.character.domain.dto.CharacterResponse;
import teamseven.echoeco.admin.character.repository.CharacterCustomRepositoryImpl;
import teamseven.echoeco.admin.character.repository.CharacterRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterCustomRepositoryImpl characterCustomRepository;

    public void save(Character character) {
        characterRepository.save(character);
    }

    public List<Character> findAll() {
        return characterRepository.findAll();
    }

    public Character findOne(Long id) {
        return characterRepository.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        characterRepository.deleteById(id);
    }

    public List<CharacterResponse> pickList(CharacterType type, Boolean isPossible) {
        return characterCustomRepository.searchPickList(type, isPossible);
    }
}
