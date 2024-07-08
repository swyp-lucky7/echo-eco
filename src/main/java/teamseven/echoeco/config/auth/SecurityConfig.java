package teamseven.echoeco.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import teamseven.echoeco.config.jwt.CustomSuccessHandler;
import teamseven.echoeco.config.jwt.JwtFilter;
import teamseven.echoeco.config.jwt.JwtUtil;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.service.CustomOAuth2UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig
{
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;
    private final AutoLoginFilter autoLoginFilter;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/vendor/**","/error", "/favicon.ico");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .csrf(AbstractHttpConfigurer::disable)// csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }))
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
//                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
//                .headers(headerConfig -> headerConfig.frameOptions(
//                        FrameOptionsConfig::disable // X-Frame-Options 비활성화
//                ))
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                .authorizeHttpRequests((auth) ->auth
                        .requestMatchers("/", "/user/login", "/file/**",
                                "/users/**", "/admin/**" // 임시로 permitAll 로 열어둠. 변경필요함.
                        ).permitAll()
                        .requestMatchers("/my").hasRole(Role.ADMIN.name())
                        .requestMatchers("/my2").authenticated()
                )

                // 권한 설정 주석처리. 변경필요.
                //.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                //       .requestMatchers("/admin/**").hasRole(Role.ADMIN.name()))
                //.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                //        .requestMatchers("").hasRole(Role.USER.name()))
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/user/login")
                        // 커스텀한 서비스 클래스를 설정
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2UserService))
                        .successHandler(customSuccessHandler)
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/user/login");
                }));

        // AutoLoginFilter를 SecurityFilterChain에 추가
        http.addFilterAfter(autoLoginFilter, JwtFilter.class);

        return http.build();
    }
}
