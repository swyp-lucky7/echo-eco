package teamseven.echoeco.admin.creature.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.admin.creature.domain.Creature;
import teamseven.echoeco.admin.creature.domain.CreatureDetail;
import teamseven.echoeco.admin.creature.domain.CreatureType;
import teamseven.echoeco.admin.creature.repository.CreatureDetailRepository;
import teamseven.echoeco.admin.creature.repository.CreatureRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CreatureServiceTest {
    @Autowired
    private CreatureRepository creatureRepository;

    private CreatureService creatureService;

    @BeforeEach
    void setUp() {
        creatureService = new CreatureService(creatureRepository);
    }

    @Test
    @DisplayName("받은 생물 엔티티를 저장할 수 있어야 한다.")
    public void givenEntity_whenCreate_thenSave() {
        //given
        Creature creature = Creature.builder()
                .name("볼리베어")
                .type(CreatureType.ANIMAL)
                .maxLevel(100)
                .description("곰")
                .build();

        //when
        creatureService.save(creature);

        //then
        List<Creature> all = creatureRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(creature.getId(), all.get(0).getId());
    }
}