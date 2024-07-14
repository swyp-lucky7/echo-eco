package teamseven.echoeco.character.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.background.service.BackgroundService;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.character.domain.*;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.dto.CharacterResponse;
import teamseven.echoeco.character.domain.dto.CharacterUserResponse;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.user.domain.User;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterUserRepository characterUserRepository;
    private final CharacterDetailService characterDetailService;
    private final BackgroundService backgroundService;
    private final TrashUserService trashUserService;

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
        return characterRepository.searchPickList(type, isPossible);
    }

    public Character pick(Long characterId, User user) throws Exception {
        Character character = characterRepository.findById(characterId).orElseThrow();
        boolean isUse = characterUserRepository.IsUse(user);
        if (isUse) {
            throw new IllegalArgumentException("이미 사용중인 캐릭터가 있습니다.");
        }
        CharacterUser characterUser = CharacterUser.builder()
                .name("")
                .character(character)
                .user(user)
                .level(0)
                .isUse(true)
                .build();
        characterUserRepository.save(characterUser);
        return character;
    }

    public CharacterUserResponse characterUser(User user) throws NotFoundCharacterUserException, NotAdminSettingException {
        CharacterUser characterUser = characterUserRepository.findByUserWithUse(user);
        if (characterUser == null) {
            throw new NotFoundCharacterUserException();
        }

        Environment environment = trashUserService.getEnvironment(user);
        Background background = backgroundService.findByLevelAndEnvironment(characterUser.getLevel(), environment);
        CharacterDetail characterDetail = characterDetailService.findByLevelAndEnvironment(characterUser.getLevel(), environment);
        return CharacterUserResponse.fromEntity(characterUser, environment, background.getImage(), characterDetail.getImageUrl());
    }

    // 아이템 구매에 따른 캐릭터 레벨 증가를 위해 추가함.
    public CharacterUser updateUserCharacter(User user, int level) {
        CharacterUser byUser = characterUserRepository.findByUser(user);
        CharacterUser characterUser = byUser.updateLevel(level);
        characterUserRepository.save(characterUser);
        return characterUser;
    }
}
