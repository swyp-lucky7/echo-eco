package teamseven.echoeco.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;

    public User getUser(Authentication authentication) {
        CustomOauth2User principal = (CustomOauth2User) authentication.getPrincipal();
        String userEmail = principal.getEmail();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 유저 이메일 입니다."));
    }
}
