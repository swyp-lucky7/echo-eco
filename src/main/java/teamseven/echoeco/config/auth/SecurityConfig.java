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
                        .requestMatchers("/", "/user/login", "/user/test", "/admin/**").permitAll())
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                .formLogin(formLogin -> formLogin.loginPage("/user/login"))
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    System.out.println(accessDeniedException.toString());
                   response.sendRedirect("/user/login");
                }))
                .oauth2Login(Customizer.withDefaults());

        // AutoLoginFilter를 SecurityFilterChain에 추가
        http.addFilterBefore(autoLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
