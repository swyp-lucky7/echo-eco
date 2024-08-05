package teamseven.echoeco.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Mock
    private DefaultOAuth2UserService defaultOAuth2UserService;

    private User normalUser;
    private User adminuser;
    private CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    void setUp() {
        customOAuth2UserService = new CustomOAuth2UserService(userRepository, userPointRepository, defaultOAuth2UserService);

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

    @Test
    @DisplayName("두명의 User 를 저장한 후 findUsers 로 찾으면 2명의 유저가 찾아진다.")
    void givenTwoPeople_whenFindAll_thenReturnTwoPeople() {
        List<User> users = customOAuth2UserService.findUsers();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Admin 유저로 UserDto를 만든 후 normalUser를 업데이트하면 normalUser의 권한이 Admin 으로 변경된다.")
    void givenUserDto_whenUpdateUserRole_thenChangeRole() {
        UserDto adminUserDto = UserDto.from(adminuser);
        customOAuth2UserService.updateUserRole(normalUser.getId(), adminUserDto);
        assertThat(userRepository.findById(normalUser.getId()).get().getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("로그인으로 유저 정보를 가져다주면 올바른 oauth2 유저 정보를 줘야 한다.")
    void givenLoginData_whenLoadUser_thenReturnOAuth2User() {
        // Given
        Map<String, Object> attributes = Map.of(
                "sub", "1234567890",
                "name", "John Doe",
                "email", "johndoe@example.com",
                "picture", "http://example.com/johndoe.jpg"
        );
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        when(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class))).thenReturn(oAuth2User);
        OAuth2UserRequest userRequest = mockOAuth2UserRequest("google", "sub");

        // When
        OAuth2User result = customOAuth2UserService.loadUser(userRequest);

        // Then
        assertNotNull(result);
        assertEquals("johndoe@example.com", result.getAttribute("email"));
    }

    private OAuth2UserRequest mockOAuth2UserRequest(String registrationId, String userNameAttributeName) {
        ClientRegistration clientRegistration = mock(ClientRegistration.class);
        ClientRegistration.ProviderDetails providerDetails = mock(ClientRegistration.ProviderDetails.class);
        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = mock(ClientRegistration.ProviderDetails.UserInfoEndpoint.class);

        when(clientRegistration.getRegistrationId()).thenReturn(registrationId);
        when(clientRegistration.getProviderDetails()).thenReturn(providerDetails);
        when(providerDetails.getUserInfoEndpoint()).thenReturn(userInfoEndpoint);
        when(userInfoEndpoint.getUserNameAttributeName()).thenReturn(userNameAttributeName);

        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        when(userRequest.getClientRegistration()).thenReturn(clientRegistration);

        return userRequest;
    }
}