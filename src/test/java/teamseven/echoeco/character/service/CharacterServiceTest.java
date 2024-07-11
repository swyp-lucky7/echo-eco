package teamseven.echoeco.character.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.background.repository.BackgroundRepository;
import teamseven.echoeco.trash.repository.TrashUserRepository;
import teamseven.echoeco.background.service.BackgroundService;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.character.domain.*;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.dto.CharacterResponse;
import teamseven.echoeco.character.domain.dto.CharacterUserResponse;
import teamseven.echoeco.character.repository.CharacterDetailRepository;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.character.service.CharacterDetailService;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CharacterServiceTest {
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CharacterUserRepository characterUserRepository;
    @Autowired
    private BackgroundRepository backgroundRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CharacterDetailRepository characterDetailRepository;

    @Autowired
    private TrashUserRepository trashUserRepository;
    private TrashUserService trashUserService;

    private BackgroundService backgroundService;
    private CharacterDetailService characterDetailService;
    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        backgroundService = new BackgroundService(backgroundRepository);
        characterDetailService = new CharacterDetailService(characterDetailRepository);
        trashUserService = new TrashUserService(trashUserRepository);
        characterService = new CharacterService(characterRepository, characterUserRepository, characterDetailService, backgroundService, trashUserService);
    }

    @Test
    @DisplayName("받은 생물 엔티티를 저장할 수 있어야 한다.")
    public void givenEntity_whenCreate_thenSave() {
        //given
        Character character = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .frameImage("http://")
                .pickImage("http://")
                .maxLevel(100)
                .descriptions("곰")
                .build();

        //when
        characterService.save(character);

        //then
        List<Character> all = characterRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(character.getId(), all.get(0).getId());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 type 이 없고, isPossible 이 null 이면 모든 데이터르 들고와야 한다.")
    void givenGetRequest_whenPickListApi_thenReturnAll() {
        saveDefaultCharacter();

        //when
        List<CharacterResponse> characterResponses = characterService.pickList(null, null);
        assertEquals(2, characterResponses.size());
        assertEquals("볼리베어", characterResponses.get(0).getName());
        assertEquals("식물", characterResponses.get(1).getName());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 type 이 있을때 맞는 type 데이터를 들고와야 한다.")
    void givenType_whenPickListApi_thenReturnCorrectData() {
        saveDefaultCharacter();

        //when
        List<CharacterResponse> plantResponse = characterService.pickList(CharacterType.PLANT, null);
        List<CharacterResponse> animalResponse = characterService.pickList(CharacterType.ANIMAL, null);

        //then
        assertEquals(1, plantResponse.size());
        assertEquals(CharacterType.PLANT, plantResponse.get(0).getType());

        assertEquals(1, animalResponse.size());
        assertEquals(CharacterType.ANIMAL, animalResponse.get(0).getType());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 isPossible 이 있을때 맞는 데이터를 들고와야 한다.")
    void givenIsPossible_whenPickListApi_thenReturnCorrectData() {
        saveDefaultCharacter();

        //when
        List<CharacterResponse> trueResponse = characterService.pickList(null, true);
        List<CharacterResponse> falseResponse = characterService.pickList(null, false);

        //then
        assertEquals(1, trueResponse.size());
        assertEquals("볼리베어", trueResponse.get(0).getName());
        assertTrue(trueResponse.get(0).isPossible());

        assertEquals(1, falseResponse.size());
        assertEquals("식물", falseResponse.get(0).getName());
        assertFalse(falseResponse.get(0).isPossible());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 type 과 isPossible 이 있을때 맞는 데이터를 들고와야 한다.")
    void givenTypeAndIsPossible_whenPickListApi_thenReturnCorrectData() {
        saveDefaultCharacter();

        //when
        List<CharacterResponse> response = characterService.pickList(CharacterType.ANIMAL, true);

        //then
        assertEquals(1, response.size());
        assertEquals(CharacterType.ANIMAL, response.get(0).getType());
        assertTrue(response.get(0).isPossible());
    }

    @Test
    @DisplayName("character user api 를 사용했을때 유저가 사용하고 있는 캐릭터가 없으면 에러가 발생해야 한다.")
    void givenNotExistCharacterUser_whenCharacterUserApi_thenReturnError() {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        //when
        NotFoundCharacterUserException exception = assertThrows(NotFoundCharacterUserException.class, () -> characterService.characterUser(user));

        //then
        assertEquals(exception.getClass(), NotFoundCharacterUserException.class);
        assertEquals(exception.getMessage(), "해당 유저가 사용하고 있는 캐릭터가 없습니다.");
    }


    @Test
    @DisplayName("character user api 를 사용했을때 백그라운드에서 어드민에 세팅안한 데이터가 있을 경우 에러를 반환해야 한다.")
    void givenNotSettingBackground_whenCharacterUserApi_thenReturnError() throws NotFoundCharacterUserException, NotAdminSettingException {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        CharacterUser characterUser = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        //when
        NotAdminSettingException exception = assertThrows(NotAdminSettingException.class, () -> characterService.characterUser(user));

        //then
        assertEquals(exception.getMessage(), "설정하지 않은 백그라운드 요청이 존재합니다.");
    }

    @Test
    @DisplayName("character user api 를 사용했을때 캐릭터 디테일에서 어드민에 세팅안한 데이터가 있을 경우 에러를 반환해야 한다.")
    void givenNotSettingCharacterDetail_whenCharacterUserApi_thenReturnError() throws NotFoundCharacterUserException, NotAdminSettingException {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        CharacterUser characterUser = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        Background background = Background.builder().image("https://").level(0).environment(Environment.TRASH).build();
        backgroundRepository.save(background);

        //when
        NotAdminSettingException exception = assertThrows(NotAdminSettingException.class, () -> characterService.characterUser(user));

        //then
        assertEquals(exception.getMessage(), "설정하지 않은 캐릭터 디테일 요청이 존재합니다.");
    }

    @Test
    @DisplayName("character user api 를 사용했을때 유저가 사용하고 있는 캐릭터가 있으면 캐릭터를 반환해야 한다.")
    void givenCharacterUser_whenCharacterUserApi_thenReturnError() throws NotFoundCharacterUserException, NotAdminSettingException {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        CharacterUser characterUser = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        Background background = Background.builder().image("https://").level(0).environment(Environment.TRASH).build();
        backgroundRepository.save(background);

        CharacterDetail characterDetail = CharacterDetail.builder().character(characters.get(0)).imageUrl("https://character1").environment(Environment.TRASH).level(0).build();
        characterDetailRepository.save(characterDetail);

        //when
        CharacterUserResponse characterUserResponse = characterService.characterUser(user);

        //then
        assertEquals(characters.get(0), characterUserResponse.getCharacter());
        assertEquals(background.getImage(), characterUserResponse.getBackgroundImage());
        assertEquals(characterDetail.getImageUrl(), characterUserResponse.getCharacterImage());
    }


    private List<Character> saveDefaultCharacter() {
        Character character1 = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .frameImage("http://")
                .pickImage("http://")
                .maxLevel(100)
                .descriptions("곰")
                .isPossible(true)
                .build();
        Character character2 = Character.builder()
                .name("식물")
                .type(CharacterType.PLANT)
                .frameImage("http://")
                .pickImage("http://")
                .maxLevel(25)
                .descriptions("꽃")
                .isPossible(false)
                .build();

        characterService.save(character1);
        characterService.save(character2);
        return List.of(character1, character2);
    }
}