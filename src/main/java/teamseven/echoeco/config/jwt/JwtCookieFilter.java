package teamseven.echoeco.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;
import teamseven.echoeco.user.domain.Role;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    public static final String TOKEN_NAME = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromCookie(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String name = jwtUtil.getName(token);
            String email = jwtUtil.getEmail(token);
            Role role = jwtUtil.getRole(token);

            request.setAttribute("userName", name);

            UserDto userDTO = new UserDto();
            userDTO.setName(name);
            userDTO.setRole(role);
            userDTO.setEmail(email);

            CustomOauth2User oAuth2User = new CustomOauth2User(userDTO);
            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(oAuth2User, null,
                    oAuth2User.getAuthorities());
            // 세션에 저장
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
