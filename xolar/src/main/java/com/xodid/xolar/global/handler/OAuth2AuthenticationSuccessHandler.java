package com.xodid.xolar.global.handler;

import com.xodid.xolar.global.jwt.TokenProvider;
import com.xodid.xolar.user.domain.User;
import com.xodid.xolar.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2 인증된 사용자 정보 가져오기
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        // 사용자 속성에서 이메일 추출
        String email = (String) oAuth2User.getAttributes().get("email");

        // email을 통해 데이터베이스에서 User 엔티티 조회
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // AccessToken, RefreshToken 발급
        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        // 리프레시토큰 저장
        tokenProvider.saveRefreshToken(user.getUserId(), refreshToken);

        // JSON형식 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // AccessToken, RefreshToken 토큰 전달
        String jsonResponse = String.format("{\"accessToken\":\"%s\", \"refreshToken\":\"%s\"}", accessToken, refreshToken);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}