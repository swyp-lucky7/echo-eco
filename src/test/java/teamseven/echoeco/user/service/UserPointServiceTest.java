package teamseven.echoeco.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserPointServiceTest {
    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private UserRepository userRepository;

    private UserPointService userPointService;

    private User user;

    @BeforeEach
    void setUp() {
        userPointService = new UserPointService(userPointRepository);
        user = User.builder()
                .email("test")
                .name("test")
                .role(Role.USER)
                .picture("test")
                .build();
        userRepository.save(user);
        userPointRepository.save(UserPoint.fromUser(user));
    }

    @DisplayName("user 로 userPoint 를 찾으면 해당 유저의 userPoint 가 찾아진다.")
    @Test
    void findUserTest() {
        UserPoint userPoint = userPointService.findByUser(user);
        assertThat(userPoint.getUser().getName()).isEqualTo(user.getName());
    }

    @DisplayName("포인트가 0인 user 에 100포인트를 더하면 user의 포인트가 100이 된다.")
    @Test
    void addUserPointTest() {
        userPointService.addUserPoint(user, 100);
        UserPoint userPoint = userPointService.findByUser(user);
        assertThat(userPoint.getUserPoint()).isEqualTo(100);
    }

    @DisplayName("포인트가 100인 user 에 60 포인트를 빼면 user의 포인트가 40이 된다.")
    @Test
    void subtractUserPointTest() {
        userPointService.addUserPoint(user, 100);
        userPointService.subtractUserPoint(user, 60);
        UserPoint userPoint = userPointService.findByUser(user);
        assertThat(userPoint.getUserPoint()).isEqualTo(40);
    }

    @DisplayName("포인트가 100인 유저에게 101 포인트를 빼면 IllegalArgumentException 이 발생한다.")
    @Test
    void subtractUserPointErrorTest() {
        userPointService.addUserPoint(user, 100);

        Assertions.assertThatThrownBy(() -> userPointService.subtractUserPoint(user, 101))
                .isInstanceOf(IllegalArgumentException.class);

    }





}