package teamseven.echoeco.admin.character.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterType;
import teamseven.echoeco.admin.character.domain.dto.CharacterResponse;
import teamseven.echoeco.admin.character.repository.CharacterCustomRepository;
import teamseven.echoeco.admin.character.repository.CharacterCustomRepositoryImpl;
import teamseven.echoeco.admin.character.repository.CharacterRepository;
import teamseven.echoeco.config.QuerydslConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CharacterServiceTest {
    @Autowired
    private CharacterRepository characterRepository;

    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterService(characterRepository);
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
        savePickListDefaultCharacter();

        //when
        List<CharacterResponse> characterResponses = characterService.pickList(null, null);
        assertEquals(2, characterResponses.size());
        assertEquals("볼리베어", characterResponses.get(0).getName());
        assertEquals("식물", characterResponses.get(1).getName());
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 type 이 있을때 맞는 type 데이터를 들고와야 한다.")
    void givenType_whenPickListApi_thenReturnCorrectData() {
        savePickListDefaultCharacter();

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
        savePickListDefaultCharacter();

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

    private void savePickListDefaultCharacter() {
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
    }

    @Test
    @DisplayName("pick list api 를 사용했을때 type 과 isPossible 이 있을때 맞는 데이터를 들고와야 한다.")
    void givenTypeAndIsPossible_whenPickListApi_thenReturnCorrectData() {
        savePickListDefaultCharacter();

        //when
        List<CharacterResponse> response = characterService.pickList(CharacterType.ANIMAL, true);

        //then
        assertEquals(1, response.size());
        assertEquals(CharacterType.ANIMAL, response.get(0).getType());
        assertTrue(response.get(0).isPossible());
    }
}