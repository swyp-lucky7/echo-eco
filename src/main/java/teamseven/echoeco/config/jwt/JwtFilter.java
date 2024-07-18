package teamseven.echoeco.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.Role;

// 쿠키에 있는 JWT 토큰을 세션에 저장
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String TOKEN_NAME = "Authorization";
    private static final String REDIRECT_URI_TO_LOGIN = "/user/login";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        boolean checkUser = request.getRequestURL().toString().contains("/user/check");
        boolean updateToken = request.getRequestURL().toString().contains("/user/token/update");


        if (request.getCookies() == null && !updateToken) {
            addCheckResultFotUserCheck(request, response, filterChain, checkUser, "쿠키 없음");
            if (!checkUser) {
   //             response.sendRedirect(REDIRECT_URI_TO_LOGIN);
            }
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                authorization = cookie.getValue();
            }
        }

        if (authorization == null && !updateToken) {
            addCheckResultFotUserCheck(request, response, filterChain, checkUser, "토큰 없음");
            if (!checkUser) {
    //            response.sendRedirect(REDIRECT_URI_TO_LOGIN);
            }
            return;
        }

        String token = authorization;
        if (!updateToken) {
            if (jwtUtil.isExpired(token)) {
                addCheckResultFotUserCheck(request, response, filterChain, checkUser, "토큰 만료");
                if (!checkUser) {
      //              response.sendRedirect(REDIRECT_URI_TO_LOGIN);
                }
                return;
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
}