package teamseven.echoeco.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserJWT;
import teamseven.echoeco.user.repository.UserJWTRepository;

@Service
@RequiredArgsConstructor
public class UserJWTService {
    private final UserJWTRepository userJWTRepository;
    private final UserService userService;

    public void saveOrUpdateUserJwt(String token, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        Optional<UserJWT> findJWT = userJWTRepository.findByUser(user);
        UserJWT userJWT = findJWT.orElseGet(() ->
                        UserJWT.builder()
                                .user(user)
                                .build()
                        );
        userJWT.updateToken(token);
        userJWTRepository.save(userJWT);
    }

    public String getUserToken(String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        UserJWT userJWT = userJWTRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않는 이메일 입니다."));
        return userJWT.getToken();
    }
}
