package teamseven.echoeco.character.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterDetail;
import teamseven.echoeco.character.domain.CharacterType;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.character.repository.CharacterDetailRepository;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.service.CharacterDetailService;
import teamseven.echoeco.config.QuerydslConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CharacterDetailServiceTest {

    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CharacterDetailRepository characterDetailRepository;

    private CharacterDetailService characterDetailService;

    @BeforeEach
    void setUp() {
        characterDetailService = new CharacterDetailService(characterDetailRepository);
    }


    @Test
    @DisplayName("받은 생물 디테일 엔티티를 저장할 수 있어야 한다.")
    public void givenDetailEntity_whenCreate_thenSave() {
        //given
        Character character = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .frameImage("http://")
                .pickImage("http://")
                .maxLevel(100)
                .descriptions("곰")
                .completeMessages("")
                .build();
        characterRepository.save(character);

        CharacterDetail characterDetail = CharacterDetail.builder()
                .character(character)
                .level(30)
                .imageUrl("http://image.url")
                .environment(Environment.CLEAN)
                .build();
        //when
        characterDetailService.save(characterDetail);

        //then
        List<CharacterDetail> all = characterDetailRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(characterDetail.getId(), all.get(0).getId());
        assertEquals(characterDetail.getCharacter(), character);
    }

}