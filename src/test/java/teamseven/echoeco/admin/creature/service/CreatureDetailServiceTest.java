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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CreatureDetailServiceTest {

    @Autowired
    private CreatureRepository creatureRepository;
    @Autowired
    private CreatureDetailRepository creatureDetailRepository;

    private CreatureDetailService creatureDetailService;

    @BeforeEach
    void setUp() {
        creatureDetailService = new CreatureDetailService(creatureDetailRepository);
    }


    @Test
    @DisplayName("받은 생물 디테일 엔티티를 저장할 수 있어야 한다.")
    public void givenDetailEntity_whenCreate_thenSave() {
        //given
        Creature creature = Creature.builder()
                .name("볼리베어")
                .type(CreatureType.ANIMAL)
                .maxLevel(100)
                .description("곰")
                .build();
        creatureRepository.save(creature);

        CreatureDetail creatureDetail = CreatureDetail.builder()
                .creature(creature)
                .level(30)
                .imageUrl("http://image.url")
                .build();
        //when
        creatureDetailService.save(creatureDetail);

        //then
        List<CreatureDetail> all = creatureDetailRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(creatureDetail.getId(), all.get(0).getId());
        assertEquals(creatureDetail.getCreature(), creature);
    }

}