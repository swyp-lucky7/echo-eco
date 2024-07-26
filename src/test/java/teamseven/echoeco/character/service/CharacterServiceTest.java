package teamseven.echoeco.character.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import teamseven.echoeco.background.service.BackgroundService;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.*;
import teamseven.echoeco.character.domain.dto.CharacterCompleteMessagesDto;
import teamseven.echoeco.character.domain.dto.CharacterPickListDto;
import teamseven.echoeco.character.domain.dto.CharacterUserResponse;
import teamseven.echoeco.character.repository.CharacterDetailRepository;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.trash.repository.TrashUserRepository;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserPointService;

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
    @Autowired
    private UserPointRepository userPointRepository;

    private CharacterService characterService;


    @BeforeEach
    void setUp() {
        UserPointService userPointService = new UserPointService(userPointRepository);
        BackgroundService backgroundService = new BackgroundService(backgroundRepository);
        CharacterDetailService characterDetailService = new CharacterDetailService(characterDetailRepository);
        TrashUserService trashUserService = new TrashUserService(trashUserRepository, userPointService);
        characterService = new CharacterService(characterRepository, characterUserRepository, characterDetailService, backgroundService, trashUserService, userPointService);
    }

    @Test
    @DisplayName("받은 생물 엔티티를 저장할 수 있어야 한다.")
    public void givenEntity_whenCreate_thenSave() {
        //given
        Character character = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .image("http://")
                .maxLevel(100)
                .descriptions("곰")
                .completeMessages("")
                .build();

        //when
        characterService.save(character);

        //then
        List<Character> all = characterRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(character.getId(), all.get(0).getId());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 isPossible 이 null 이면 모든 데이터를 들고와야 한다.")
    void givenGetRequest_whenPickListApi_thenReturnAll() {
        saveDefaultCharacter();

        //when
        List<CharacterPickListDto> characterResponses = characterService.pickList(null);
        assertEquals(2, characterResponses.size());
        assertEquals("볼리베어", characterResponses.get(0).getName());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 isPossible 이 있을때 맞는 데이터를 들고와야 한다.")
    void givenIsPossible_whenPickListApi_thenReturnCorrectData() {
        saveDefaultCharacter();

        //when
        List<CharacterPickListDto> trueResponse = characterService.pickList(true);
        List<CharacterPickListDto> falseResponse = characterService.pickList(false);

        //then
        assertEquals(1, trueResponse.size());
        assertEquals("볼리베어", trueResponse.get(0).getName());
        assertTrue(trueResponse.get(0).getIsPossible());

        assertEquals(1, falseResponse.size());
        assertEquals("식물", falseResponse.get(0).getName());
        assertFalse(falseResponse.get(0).getIsPossible());
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

        Background background = Background.builder().image("https://").level(0).environment(Environment.CLEAN).build();
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
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        CharacterUser characterUser = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        Background background = Background.builder().image("https://").level(0).environment(Environment.CLEAN).build();
        backgroundRepository.save(background);

        CharacterDetail characterDetail = CharacterDetail.builder().character(characters.get(0)).imageUrl("https://character1").environment(Environment.CLEAN).level(0).build();
        characterDetailRepository.save(characterDetail);

        //when
        CharacterUserResponse characterUserResponse = characterService.characterUser(user);

        //then
        assertEquals(characters.get(0), characterUserResponse.getCharacter());
        assertEquals(background.getImage(), characterUserResponse.getBackgroundImage());
        assertEquals(characterDetail.getImageUrl(), characterUserResponse.getCharacterImage());
    }

    @Test
    @DisplayName("pick 할때 이미 사용하고 있는 캐릭터가 존재하면 에러를 발생한다.")
    void givenAlreadyPickCharacter_whenPick_thenError() {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);
        CharacterUser characterUser = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> characterService.pick(characters.get(0).getId(), user));

        //then
        assertEquals("이미 사용중인 캐릭터가 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("pick 할때 캐릭터를 잘 선택했으면 픽한 캐릭터 데이터를 넘겨준다.")
    void givenFirstPickCharacter_whenPick_thenReturnCharacter() {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        //when
        Character pick = characterService.pick(characters.get(0).getId(), user);

        //then
        assertEquals(characters.get(0), pick);
    }


    @Test
    @DisplayName("complete messages api 를 사용했을때 유저가 사용하고 있는 캐릭터가 없으면 에러가 발생해야 한다.")
    void givenNotExistCharacterUser_whenCompleteMessagesApi_thenReturnError() {
        List<Character> characters = saveDefaultCharacter();
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        //when
        NotFoundCharacterUserException exception = assertThrows(NotFoundCharacterUserException.class, () -> characterService.completeMessages(user));

        //then
        assertEquals(exception.getClass(), NotFoundCharacterUserException.class);
        assertEquals(exception.getMessage(), "해당 유저가 사용하고 있는 캐릭터가 없습니다.");
    }

    @Test
    @DisplayName("complete messages api 를 사용했을때 유저가 사용하고 있는 캐릭터가 있으면 완료 메세지를 반환해야 한다.")
    void givenCharacterUser_whenCompleteMessageApi_thenReturnError() throws NotFoundCharacterUserException, JSONException {
        Character character1 = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .image("http://")
                .maxLevel(100)
                .descriptions("곰")
                .isPossible(true)
                .completeMessages("[{\"step\":\"축하1\"},{\"step\":\"축하2\"}]")
                .build();
        characterRepository.save(character1);

        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        CharacterUser characterUser = CharacterUser.builder().user(user).character(character1).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        //when
        CharacterCompleteMessagesDto characterCompleteMessagesDto = characterService.completeMessages(user);

        //then
        JSONArray jsonArray = new JSONArray(characterCompleteMessagesDto.getCompleteMessages());
        assertEquals(2, jsonArray.length());

        JSONObject jsonObject =  (JSONObject) jsonArray.get(0);
        assertEquals("축하1", jsonObject.get("step"));

        JSONObject jsonObject2 =  (JSONObject) jsonArray.get(1);
        assertEquals("축하2", jsonObject2.get("step"));
    }

    @Test
    @DisplayName("유저가 캐릭터를 완료했다고 보냈을때 사용하고 있는 캐릭터가 없으면 에러를 내야 한다.")
    void givenNoCharacter_whenComplete_thenError() {
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        List<Character> characters = saveDefaultCharacter();
        Character character = characters.get(0);
        CharacterUser characterUser = CharacterUser.builder().user(user).character(character).isUse(false).level(0).build();
        characterUserRepository.save(characterUser);

        //when
        NotFoundCharacterUserException exception = assertThrows(NotFoundCharacterUserException.class, () -> characterService.complete(user));

        //then
        assertEquals("해당 유저가 사용하고 있는 캐릭터가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유저가 캐릭터를 완료했다고 보냈을때 사용하고 있는 캐릭터의 레벨이 맥스 레벨보다 작으면 에러를 내야 한다.")
    void givenCharacterLevelLowerThanMaxLevel_whenComplete_thenError() {
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        List<Character> characters = saveDefaultCharacter();
        Character character = characters.get(0);
        CharacterUser characterUser = CharacterUser.builder().user(user).character(character).isUse(true).level(0).build();
        characterUserRepository.save(characterUser);

        //when
        IllegalCallerException exception = assertThrows(IllegalCallerException.class, () -> characterService.complete(user));

        //then
        assertEquals("해당 유저의 레벨이 만렙보다 작습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유저가 캐릭터를 완료했다고 보냈을때 isUse 가 false 되어야 한다.")
    void givenCorrectCondition_whenComplete_thenIsUseFalse() throws NotFoundCharacterUserException {
        User user = new User("user1", "email1", "picture1", Role.ADMIN);
        userRepository.save(user);

        List<Character> characters = saveDefaultCharacter();
        Character character = characters.get(0);
        CharacterUser characterUser = CharacterUser.builder().user(user).character(character).isUse(true).level(character.getMaxLevel()).build();
        characterUserRepository.save(characterUser);

        //when
        characterService.complete(user);

        //then
        assertFalse(characterUser.getIsUse());
    }

    private List<Character> saveDefaultCharacter() {
        Character character1 = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .image("http://")
                .maxLevel(100)
                .descriptions("곰")
                .isPossible(true)
                .completeMessages("")
                .build();
        Character character2 = Character.builder()
                .name("식물")
                .type(CharacterType.PLANT)
                .image("http://")
                .maxLevel(25)
                .descriptions("꽃")
                .isPossible(false)
                .completeMessages("")
                .build();

        characterService.save(character1);
        characterService.save(character2);
        return List.of(character1, character2);
    }
}