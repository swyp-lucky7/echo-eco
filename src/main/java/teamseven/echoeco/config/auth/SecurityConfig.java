package teamseven.echoeco.config.auth;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import teamseven.echoeco.config.jwt.*;
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


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                // 필터(로그인 인증) 없이 사용하는 url 추가 필요
                // /token/init 은 삭제 X
                .requestMatchers("/static/**", "/vendor/**","/error", "/favicon.ico", "/user/login/**", "/item/list", "/", "/character/list", "/user/token/**", "/token/init");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)// csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다.
                // (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://echoeco.shop.s3-website-us-east-1.amazonaws.com", "https://echo-eco.swygbro.com"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }))
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
//                .headers(headerConfig -> headerConfig.frameOptions(
//                        FrameOptionsConfig::disable // X-Frame-Options 비활성화
//                ))
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                .authorizeHttpRequests((auth) ->auth
                        .requestMatchers("/", "/user/login", "/file/**",
                                 "/user/**"  // 임시로 permitAll 로 열어둠. 변경필요함.
                        ).permitAll()
                )
                .authorizeHttpRequests((auth) -> auth.requestMatchers(
                        "/character/**", "/trash/**", "/item/**", "/question/**", "/video/**", "/contents/**", "/gifticon/**"
                )
                        //Todo: 배포시 아래 주석 삭제 필요
                                //.hasRole(Role.USER.name())
                                .permitAll()
                )

                // 권한 설정 주석처리. 변경필요.
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                       .requestMatchers("/admin/**").hasRole(Role.ADMIN.name()))
                //.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                //        .requestMatchers("").hasRole(Role.USER.name()))
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/")
                        .deleteCookies("Authorization", "remember-me")
                        )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/user/login")
                        // 커스텀한 서비스 클래스를 설정
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2UserService))
                        .successHandler(customSuccessHandler)
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtCookieFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtFilter.class)
// ToDo: 배포시 아래 주석 삭제 필요.
     //           .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
      //              response.sendRedirect("/user/login");
       //         }))
        ;

        return http.build();
    }
}
