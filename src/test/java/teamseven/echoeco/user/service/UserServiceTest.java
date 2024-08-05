package teamseven.echoeco.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserServiceTest {
    UserService userService;
    @Mock
    private Authentication authentication;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }
    @Test
    @DisplayName("Authentication 으로 받은 email 로 검색시 데이터가 없으면 에러를 반환해야 한다.")
    void givenAuthentication_whenGetUser_thenError() {
        String email = "email1";
        User user = new User("user1", email, "picture1", Role.ADMIN);

        UserDto userDto = new UserDto();
        userDto.updateUserForm(user);

        CustomOauth2User customOauth2User = new CustomOauth2User(userDto);
        when(authentication.getPrincipal()).thenReturn(customOauth2User);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getUser(authentication));

        //then
        assertEquals("잘못된 유저 이메일 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Authentication 으로 user 요청시 user 를 반환해야 한다.")
    void givenAuthentication_whenGetUser_thenReturnUser() {
        String email = "email1";
        User user = new User("user1", email, "picture1", Role.ADMIN);
        userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.updateUserForm(user);

        CustomOauth2User customOauth2User = new CustomOauth2User(userDto);
        when(authentication.getPrincipal()).thenReturn(customOauth2User);

        //when
        User find = userService.getUser(authentication);

        //then
        assertEquals(user, find);
    }
}