package teamseven.echoeco.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import teamseven.echoeco.config.exception.InvalidUserException;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.Role;

// 쿠키에 있는 JWT 토큰을 세션에 저장
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String TOKEN_NAME = "authorization";
    private static final String REDIRECT_URI_TO_LOGIN = "/user/login";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean hasToken = false;
        boolean checkUser = request.getRequestURL().toString().contains("/user/check");
        boolean updateToken = request.getRequestURL().toString().contains("/user/token/update");

        Iterator<String> headerNameIterator = request.getHeaderNames().asIterator();
        while (headerNameIterator.hasNext()) {
            String headerName = headerNameIterator.next();
            if (headerName.equals(TOKEN_NAME)) {
                hasToken = true;
            }
        }

        if (!hasToken && !updateToken) {
            throw new InvalidUserException("유저 정보 토큰이 없습니다.");
        }

        String token = request.getHeader(TOKEN_NAME);
        if (!updateToken) {
            if (jwtUtil.isExpired(token)) {
                throw new InvalidUserException("토큰이 만료되었습니다.");
            }
        }

        if (checkUser) {
            request.setAttribute("userCheck", true);
            request.setAttribute("userCheckDetail", "검증 완료");
        }

        if (updateToken) {
            request.setAttribute("token", token);
        }

        if (!updateToken) {
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

    private static void addCheckResultFotUserCheck(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                  boolean checkUser, String checkResult) throws IOException, ServletException {
        if (checkUser) {
            request.setAttribute("userCheck", false);
            request.setAttribute("userCheckDetail", checkResult);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURL().toString().contains("/admin");
    }
}