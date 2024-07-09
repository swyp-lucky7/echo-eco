package teamseven.echoeco.admin.background.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.admin.character.domain.Environment;
import teamseven.echoeco.admin.background.domain.Background;
import teamseven.echoeco.admin.background.repository.BackgroundRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BackgroundServiceTest {
    @Autowired
    private BackgroundRepository backgroundRepository;

    private BackgroundService backgroundService;

    @BeforeEach
    void setUp() {
        backgroundService = new BackgroundService(backgroundRepository);
    }

    @Test
    @DisplayName("엔티티 저장시 잘 저장이 되어야 한다.")
    public void givenEntity_whenSave_thenSuccess() {
        Background background = Background.builder()
                .name("name1")
                .level(10)
                .image("http")
                .environment(Environment.CLEAN)
                .build();

        //when
        backgroundRepository.save(background);

        //then
        List<Background> all = backgroundRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(background, all.get(0));
    }

    @Test
    @DisplayName("엔티티 전체 검색시 전체를 되돌려줘야 한다.")
    public void givenEntities_whenFindAll_thenList() {
        Background background1 = Background.builder()
                .name("name1")
                .level(10)
                .image("http")
                .environment(Environment.CLEAN)
                .build();
        Background background2 = Background.builder()
                .name("name2")
                .level(20)
                .image("http")
                .environment(Environment.TRASH)
                .build();

        backgroundRepository.save(background1);
        backgroundRepository.save(background2);

        //when
        List<Background> all = backgroundService.findAll();

        //then
        assertEquals(2, all.size());
        assertTrue(all.contains(background1));
        assertTrue(all.contains(background2));
    }

    @Test
    @DisplayName("id 로 검색시 하나가 나와야 한다.")
    public void givenEntity_whenFindById_thenEntity() {
        Background background1 = Background.builder()
                .name("name1")
                .level(10)
                .image("http")
                .environment(Environment.CLEAN)
                .build();
        Background background2 = Background.builder()
                .name("name2")
                .level(20)
                .image("http")
                .environment(Environment.TRASH)
                .build();

        backgroundRepository.save(background1);
        backgroundRepository.save(background2);

        //when
        Background find = backgroundService.findById(background1.getId());

        //then
        assertEquals(find, background1);
    }
//
//    @Test
//    @DisplayName("id 로 검색시 없을시 에러가 발생해야 한다.")
//    public void givenEntity_whenFindById_thenEntity() {
//        Background background1 = Background.builder()
//                .name("name1")
//                .level(10)
//                .backgroundUrl("http")
//                .environment(Environment.CLEAN)
//                .build();
//        Background background2 = Background.builder()
//                .name("name2")
//                .level(20)
//                .backgroundUrl("http")
//                .environment(Environment.TRASH)
//                .build();
//
//        backgroundRepository.save(background1);
//        backgroundRepository.save(background2);
//
//        //when
//        Background find = backgroundService.findById(1L);
//
//        //then
//        assertEquals(find, background1);
//    }
}