package teamseven.echoeco.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.InvalidUserException;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (InvalidUserException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ApiResponse<String> loginError = ApiResponse.res(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), "login error");

            response.getWriter().write(objectMapper.writeValueAsString(loginError));
        }
    }
}
