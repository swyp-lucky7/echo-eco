package teamseven.echoeco.user.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import teamseven.echoeco.user.domain.OAuth2.OAuthAttributes;
import teamseven.echoeco.user.domain.OAuth2.Oauth2UserImpl;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.repository.UserRepository;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        return new Oauth2UserImpl(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey(), UserDto.from(user));
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),attributes.getPicture(), entity.getRole()))
                .orElseGet(attributes::toEntity);

        return userRepository.save(user);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User findOneById(Long id) {
        // Optional.get() 실패할 경우 추가 필요
        return userRepository.findById(id).get();
    }

    public void updateUserRole(Long userId, UserDto userDto) {
        User findUser = findOneById(userId);
        findUser.updateRole(userDto.getRole());
        userRepository.save(findUser);
    }
}
