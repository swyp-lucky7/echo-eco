package teamseven.echoeco.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.OAuth2.Oauth2UserImpl;
import teamseven.echoeco.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 로그인 성공 시 호출
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDto userInfo = ((Oauth2UserImpl) authentication.getPrincipal()).getUserDto();
        String name = userInfo.getName();
        String role = userInfo.getRole().name();
        String email = userInfo.getEmail();
        // Todo
        // expiredMs 수정 필요
        String token = jwtUtil.createJwt(name, role, email, 60 * 60 * 1000L);
        // 토큰을 쿠키에 저장, 키는 "Authorization"

        response.addCookie(createCookie("Authorization", token));
        response.addHeader("Authorization", token);
        // Todo
        // 프론트 서버로 변경 필요, 유저 동물생성 유무에 따라 캐릭터선택 or 스테이지로 변경 필요
        response.sendRedirect("http://localhost:3000");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
//        cookie.setSecure(true); // HTTPS 환경
        cookie.setPath("/");
        //cookie.setDomain("localhost");
      //  cookie.setHttpOnly(true);
        return cookie;
    }
}