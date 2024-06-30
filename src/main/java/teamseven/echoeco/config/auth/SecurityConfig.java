package teamseven.echoeco.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import teamseven.echoeco.login.CustomOAuth2UserService;
import teamseven.echoeco.user.Role;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig
{
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AutoLoginFilter autoLoginFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/vendor/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headerConfig -> headerConfig.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                ))
                .authorizeHttpRequests((auth) ->auth
                        .requestMatchers("/", "/user/login",
                                "/users/**", "/admin/**" // 임시로 permitAll 로 열어둠. 변경필요함.
                        ).permitAll())
                // 권한 설정 주석처리. 변경필요.
                //.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                //       .requestMatchers("/admin/**").hasRole(Role.ADMIN.name()))
                //.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                //        .requestMatchers("").hasRole(Role.USER.name()))
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                .formLogin(formLogin -> formLogin.loginPage("/user/login"))
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/user/login");
                }))
                .oauth2Login(Customizer.withDefaults());

        // AutoLoginFilter를 SecurityFilterChain에 추가
        http.addFilterBefore(autoLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
