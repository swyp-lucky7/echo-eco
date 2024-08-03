package teamseven.echoeco.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    private User normalUser;
    private User adminuser;
    private CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    void setUp() {
        customOAuth2UserService = new CustomOAuth2UserService(userRepository,
                userPointRepository);
        normalUser = User.builder()
                .email("test")
                .name("test")
                .role(Role.USER)
                .picture("test")
                .build();

        adminuser = User.builder()
                .email("test2")
                .name("test2")
                .role(Role.ADMIN)
                .picture("test2")
                .build();
        userRepository.save(normalUser);
        userRepository.save(adminuser);
    }

    @DisplayName("두명의 User 를 저장한 후 findUsers 로 찾으면 2명의 유저가 찾아진다.")
    @Test
    void findAllTest() {
        List<User> users = customOAuth2UserService.findUsers();
        assertThat(users.size()).isEqualTo(2);
    }

    @DisplayName("Admin 유저로 UserDto를 만든 후 normalUser를 업데이트하면 normalUser의 권한이 Admin 으로 변경된다.")
    @Test
    void updateRoleTest() {
        UserDto adminUserDto = UserDto.from(adminuser);
        customOAuth2UserService.updateUserRole(normalUser.getId(), adminUserDto);
        assertThat(userRepository.findById(normalUser.getId()).get().getRole()).isEqualTo(Role.ADMIN);
    }



}