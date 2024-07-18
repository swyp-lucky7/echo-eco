package teamseven.echoeco.trash.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.exception.AlreadyCleanTrashException;
import teamseven.echoeco.trash.domain.TrashUser;
import teamseven.echoeco.trash.domain.dto.TrashStatusDto;
import teamseven.echoeco.trash.repository.TrashUserRepository;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.user.domain.Dto.UserPointDto;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserPointService;

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
    @Autowired
    private UserPointRepository userPointRepository;

    @BeforeEach
    void setUp() {
        UserPointService userPointService = new UserPointService(userPointRepository);
        trashUserService = new TrashUserService(trashUserRepository, userPointService);
    }

    @Test
    @DisplayName("쓰레기를 청소했을 때 오늘 이미 청소했으면 에러가 발생해야 한다.")
    void givenAlreadyTrashClean_whenCleanTrash_ThenError() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        TrashUser trashUser = TrashUser.builder().user(user).build();
        trashUserRepository.save(trashUser);

        //when
        AlreadyCleanTrashException exception = assertThrows(AlreadyCleanTrashException.class, () -> trashUserService.cleanTrash(user));

        //then
        assertEquals(exception.getMessage(), "이미 오늘 쓰레기 청소를 한 유저입니다.");
    }

    @Test
    @DisplayName("처음 들어온 유저가 쓰레기를 청소했을 때 청소 데이터가 잘 저장되어야 한다.")
    void givenFirstUser_whenCleanTrash_ThenSuccess() throws AlreadyCleanTrashException {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        //when
        trashUserService.cleanTrash(user);

        //then
        List<TrashUser> all = trashUserRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(user, all.get(0).getUser());
    }

    @Test
    @DisplayName("어제 치운 후 다시 쓰레기를 청소했을 때 청소 데이터가 잘 저장되어야 한다.")
    void givenYesterdayClean_whenCleanTrash_ThenSuccess() throws AlreadyCleanTrashException {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        TrashUser trashUser = TrashUser.builder().user(user).updated_at(LocalDateTime.now().minusDays(1)).build();
        trashUserRepository.save(trashUser);

        //when
        trashUserService.cleanTrash(user);

        //then
        List<TrashUser> all = trashUserRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(user, all.get(0).getUser());
    }

    @Test
    @DisplayName("청소가 잘되었을 때 20 포인트가 올라가야 한다.")
    void givenUserPoint_whenCleanTrash_ThenAdd20Point() throws AlreadyCleanTrashException {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);
        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        TrashUser trashUser = TrashUser.builder().user(user).updated_at(LocalDateTime.now().minusDays(1)).build();
        trashUserRepository.save(trashUser);

        //when
        UserPointDto userPointDto = trashUserService.cleanTrash(user);

        //then
        List<TrashUser> all = trashUserRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(user, all.get(0).getUser());
        assertEquals(20, userPointDto.getAddPoint());
        assertEquals(20, userPointDto.getAfterPoint());
    }

    @Test
    @DisplayName("쓰레기를 처음 치우면 isClearTheTrash 에서 False 를 얻어야 한다.")
    void givenFirstTime_whenIsTodayCleanTrash_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        //when
        boolean clearTheTrash = trashUserService.isTodayCleanTrash(user);

        //then
        assertFalse(clearTheTrash);
    }

    @Test
    @DisplayName("쓰레기를 어제 치웠고 오늘 안치웠으면 isClearTheTrash 에서 False 를 얻어야 한다.")
    void givenTrashUser_whenIsTodayCleanTrash_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).updated_at(LocalDateTime.now().minusDays(1)).build();
        trashUserService.save(trashUser);

        //when
        boolean clearTheTrash = trashUserService.isTodayCleanTrash(user);

        //then
        assertFalse(clearTheTrash);
    }

    @Test
    @DisplayName("쓰레기를 오늘 치웠으면 isClearTheTrash 에서 true 를 얻어야 한다.")
    void givenAlreadyTrashClear_whenIsTodayCleanTrash_ThenTrue() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).build();
        trashUserService.save(trashUser);

        //when
        boolean clearTheTrash = trashUserService.isTodayCleanTrash(user);

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

    @Test
    @DisplayName("쓰레기를 치웠으면 isClean 은 true 여야한다.")
    void givenAlreadyTrashClear_whenTrashStatus_ThenTrue() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).build();
        trashUserService.save(trashUser);

        //when
        TrashStatusDto trashStatusDto = trashUserService.trashStatus(user);

        //then
        assertEquals(true, trashStatusDto.getIsClean());
    }
    @Test
    @DisplayName("쓰레기를 치우지 않았으면 isClean 은 false 여야한다.")
    void givenNotTrashClear_whenTrashStatus_ThenFalse() {
        User user = User.builder().name("user1").email("email1").picture("picture1").role(Role.ADMIN).build();
        userRepository.save(user);

        TrashUser trashUser = TrashUser.builder().user(user).updated_at(LocalDateTime.now().minusDays(1)).build();
        trashUserService.save(trashUser);

        //when
        TrashStatusDto trashStatusDto = trashUserService.trashStatus(user);

        //then
        assertEquals(false, trashStatusDto.getIsClean());
    }
}