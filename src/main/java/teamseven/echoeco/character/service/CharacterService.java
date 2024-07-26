package teamseven.echoeco.character.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.background.service.BackgroundService;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterDetail;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.character.domain.dto.*;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.service.UserPointService;

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
    private final UserPointService userPointService;

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

    public List<CharacterPickListDto> pickList(Boolean isPossible) {
        return characterRepository.searchPickList(isPossible);
    }

    public Character pick(Long characterId, User user) throws IllegalArgumentException {
        Character character = characterRepository.findById(characterId).orElseThrow();
        boolean isUse = characterUserRepository.IsUse(user);
        if (isUse) {
            throw new IllegalArgumentException("이미 사용중인 캐릭터가 있습니다.");
        }
        CharacterUser characterUser = CharacterUser.builder()
                .name("")
                .character(character)
                .user(user)
                .level(1)
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

        Background background = backgroundService.findByLevelAndEnvironment(characterUser.getLevel(), Environment.CLEAN);
        CharacterDetail characterDetail = characterDetailService.findByLevelAndEnvironment(characterUser.getLevel(), Environment.CLEAN);

        //userPoint
        UserPoint userPoint = userPointService.findByUser(user);

        return CharacterUserResponse.fromEntity(
                characterUser,
                Environment.CLEAN,
                background.getImage(),
                characterDetail.getImageUrl(),
                userPoint
        );
    }

    public CharacterCompleteMessagesDto completeMessages(User user) throws NotFoundCharacterUserException {
        CharacterUser characterUser = characterUserRepository.findByUserWithUse(user);
        if (characterUser == null) {
            throw new NotFoundCharacterUserException();
        }

        Character character = characterUser.getCharacter();
        return CharacterCompleteMessagesDto.builder().completeMessages(character.getCompleteMessages()).build();
    }

    public CharacterTrashDto characterTrash(User user) throws NotFoundCharacterUserException, NotAdminSettingException {
        CharacterUser characterUser = characterUserRepository.findByUserWithUse(user);
        if (characterUser == null) {
            throw new NotFoundCharacterUserException();
        }

        Background background = backgroundService.findByLevelAndEnvironment(characterUser.getLevel(), Environment.TRASH);
        CharacterDetail characterDetail = characterDetailService.findByLevelAndEnvironment(characterUser.getLevel(), Environment.TRASH);
        return CharacterTrashDto.builder()
                .characterImage(characterDetail.getImageUrl())
                .backgroundImage(background.getImage())
                .build();
    }

    public CharacterUser addUserCharacter(User user, int level) {
        CharacterUser characterUser = characterUserRepository.findByUser(user);
        characterUser.addLevel(level);
        characterUserRepository.save(characterUser);
        return characterUser;
    }

    public void complete(User user) throws NotFoundCharacterUserException {
        CharacterUser characterUser = characterUserRepository.findByUserWithUse(user);
        if (characterUser == null) {
            throw new NotFoundCharacterUserException();
        }

        Character character = characterUser.getCharacter();
        if (characterUser.getLevel() < character.getMaxLevel()) {
            throw new IllegalCallerException("해당 유저의 레벨이 만렙보다 작습니다.");
        }

        characterUser.complete();
        characterUserRepository.save(characterUser);
    }
}
