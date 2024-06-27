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
import teamseven.echoeco.login.CustomOAuth2UserService;
import teamseven.echoeco.user.Role;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig
{
    private final CustomOAuth2UserService customOAuth2UserService;

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
                        .requestMatchers("/", "/user/login", "/user/test"
                        ).permitAll())
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/test").hasRole(Role.USER.name()))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name()))
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                .formLogin(formLogin -> formLogin.loginPage("/user/login"))
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    System.out.println(accessDeniedException.toString());
                   response.sendRedirect("/user/login");
                }))
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
