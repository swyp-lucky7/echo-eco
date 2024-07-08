package teamseven.echoeco.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    // 로그인 성공 시 호출
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getAttribute("name");
        String role = oAuth2User.getAuthorities().iterator().next().getAuthority();
        System.out.println("test:" + role);
        // Todo
        // expiredMs 수정 필요
        String token = jwtUtil.createJwt(name, role, 60 * 60 * 1000L);
        // 토큰을 쿠키에 저장, 키는 "Authorization"
        response.addCookie(createCookie("Authorization", token));
        // Todo
        // 프론트 서버로 변경 필요
        response.sendRedirect("http://localhost:3000/");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
//        cookie.setSecure(true); // HTTPS 환경
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}