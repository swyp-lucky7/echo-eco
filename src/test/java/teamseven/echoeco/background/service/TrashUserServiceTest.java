package teamseven.echoeco.background.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.background.domain.TrashUser;
import teamseven.echoeco.background.repository.TrashUserRepository;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TrashUserServiceTest {
    @Autowired
    private TrashUserRepository trashUserRepository;
    @Autowired
    private UserRepository userRepository;

    private TrashUserService trashUserService;

    @BeforeEach
    void setUp() {
        trashUserService = new TrashUserService(trashUserRepository);
    }

    @Test
    @DisplayName("쓰레기 관련 정보가 잘 저장이 되어야 한다.")
    void givenTrashUser_whenSave_ThenSave() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).build();

        //when
        trashUserService.save(trashUser);

        //then
        List<TrashUser> all = trashUserRepository.findAll();
        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getUser(), user);
    }

    @Test
    @DisplayName("쓰레기를 처음 치우면 isClearTheTrash 에서 False 를 얻어야 한다.")
    void givenFirstTime_whenIsClearTheTrash_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        //when
        boolean clearTheTrash = trashUserService.isClearTheTrash(user);

        //then
        assertFalse(clearTheTrash);
    }

    @Test
    @DisplayName("쓰레기를 어제 치웠고 오늘 안치웠으면 isClearTheTrash 에서 False 를 얻어야 한다.")
    void givenTrashUser_whenIsClearTheTrash_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).updated_at(LocalDateTime.now().minusDays(1)).build();
        trashUserService.save(trashUser);

        //when
        boolean clearTheTrash = trashUserService.isClearTheTrash(user);

        //then
        assertFalse(clearTheTrash);
    }

    @Test
    @DisplayName("쓰레기를 오늘 치웠으면 isClearTheTrash 에서 true 를 얻어야 한다.")
    void givenAlreadyTrashClear_whenIsClearTheTrash_ThenTrue() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).build();
        trashUserService.save(trashUser);

        //when
        boolean clearTheTrash = trashUserService.isClearTheTrash(user);

        //then
        assertTrue(clearTheTrash);
    }

    @Test
    @DisplayName("쓰레기를 처음 치우면 환경값은 TRASH 이다")
    void givenFirstTime_whenGetEnvironment_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        //when
        Environment environment = trashUserService.getEnvironment(user);

        //then
        assertEquals(Environment.TRASH, environment);
    }

    @Test
    @DisplayName("쓰레기를 어제 치웠고 오늘 안치웠으면 환경값이 TRASH 여야 한다.")
    void givenTrashUser_whenGetEnvironment_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).updated_at(LocalDateTime.now().minusDays(1)).build();
        trashUserService.save(trashUser);

        //when
        Environment environment = trashUserService.getEnvironment(user);

        //then
        assertEquals(Environment.TRASH, environment);
    }

    @Test
    @DisplayName("쓰레기를 오늘 치웠으면 환경값이 CLEAN 여야 한다.")
    void givenAlreadyTrashClear_whenGetEnvironment_ThenTrue() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).build();
        trashUserService.save(trashUser);

        //when
        Environment environment = trashUserService.getEnvironment(user);

        //then
        assertEquals(Environment.CLEAN, environment);
    }
}