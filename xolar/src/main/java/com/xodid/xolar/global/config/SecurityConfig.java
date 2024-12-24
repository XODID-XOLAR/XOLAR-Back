package com.xodid.xolar.global.config;

import com.xodid.xolar.global.handler.OAuth2AuthenticationSuccessHandler;
import com.xodid.xolar.global.jwt.JwtAuthenticationFilter;
import com.xodid.xolar.global.jwt.TokenProvider;
import com.xodid.xolar.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /**
     * CORS 설정을 위한 Bean 등록
     * 프론트엔드가 위치한 도메인과 CORS 통신을 허용하기 위해 설정함
     *
     * @return CORS 설정이 적용된 CorsConfigurationSource 객체
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://xolar.co.kr"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * SecurityFilterChain 설정을 위한 Bean 등록
     * HTTP 요청에 대한 보안 구성을 정의
     *
     * @param http HttpSecurity 설정 객체
     * @return 설정이 완료된 SecurityFilterChain 객체
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 기본 인증 방식 비활성화 (UI 대신 토큰을 통한 인증을 사용하기 때문)
                .httpBasic(AbstractHttpConfigurer::disable)
                // CSRF 보호 비활성화 (토큰 기반 인증이므로 필요하지 않음)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 설정 적용
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                // 요청에 따른 인증 인가 설정
                .authorizeHttpRequests(requests -> {
                    /* 액세스토큰 재발급은 허용 */
                    requests.requestMatchers("auth/token").permitAll();
                    /* 다른 모든 요청은 인증을 요구 */
                    requests.anyRequest().authenticated();
                })
                // JWT를 사용하므로 sateless
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 인증 필터를 UsernamePAsswordAuthenticationFilter 앞에 추가하여 JWT를 통한 인증 수행
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                // OAuth2 로그인 설정 - 인증된 사용자 정보(프로필)를 가져오는 방식 정의, 인증 성공시 동작을 정의하는 successHandler 설정
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler))
                .build();
    }
}