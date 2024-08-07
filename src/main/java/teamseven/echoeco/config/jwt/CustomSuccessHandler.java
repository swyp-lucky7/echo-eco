package teamseven.echoeco.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.OAuth2.Oauth2UserImpl;
import teamseven.echoeco.user.domain.Role;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${admin.page.domain}")
    private String adminPageDomain;

    @Value("${front.server.domain}")
    private String frontServerDomain;

    private final JwtUtil jwtUtil;
    // 로그인 성공 시 호출
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDto userInfo = ((Oauth2UserImpl) authentication.getPrincipal()).getUserDto();
        String name = userInfo.getName();
        String role = userInfo.getRole().name();
        String email = userInfo.getEmail();
        // Todo
        // expiredMs 수정 필요
        String token = jwtUtil.createJwt(name, role, email, 24 * 60 * 60 * 1000L);

        response.addHeader(JwtFilter.TOKEN_NAME, token);

//        headers.add("Access-Control-Expose-Headers", "token");
//        response.addCookie(createCookie("Authorization", token));
        if (userInfo.getRole() == Role.ADMIN) {
            response.sendRedirect(adminPageDomain + "/token/init?token=" + token);
        } else {
            response.sendRedirect( frontServerDomain + "/loginwait?token=" + token);
        }
    }

//    private Cookie createCookie(String key, String value) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(60 * 60 * 60);
////        cookie.setSecure(true); // HTTPS 환경
//        cookie.setPath("/");
//        cookie.setDomain("http://localhost:3000");
//      //  cookie.setHttpOnly(true);
//        return cookie;
//    }

}