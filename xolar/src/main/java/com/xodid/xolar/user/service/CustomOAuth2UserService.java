package com.xodid.xolar.user.service;

import com.xodid.xolar.user.domain.SocialType;
import com.xodid.xolar.user.domain.User;
import com.xodid.xolar.user.oauth.GoogleOAuth2UserInfo;
import com.xodid.xolar.user.oauth.KakaoOAuth2UserInfo;
import com.xodid.xolar.user.oauth.OAuth2UserInfo;
import com.xodid.xolar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    /**
     * OAuth2UserRequest를 받아 사용자를 로드하는 메서드
     *
     * @param userRequest OAuth2UserRequest 객체로, OAuth2 클라이언트 정보가 담겨 있음
     * @return OAuth2User 인증된 사용자 정보 객체
     * @throws OAuth2AuthenticationException OAuth 인증 실패 시 예외 발생
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 사용자 정보 로드
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // OAuth2 제공자(Google, Kakao)의 ID를 통해 SocialType 결정
        SocialType socialType = SocialType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo;

        // 소셜 로그인 타입에 따라 OAuth2UserInfo 객체 생성
        if (socialType == SocialType.GOOGLE) {
            oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        } else if (socialType == SocialType.KAKAO) {
            oAuth2UserInfo = new KakaoOAuth2UserInfo(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // DB에서 해당 사용자 조회 -> 없으면 새로 생성
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(() -> createUser(oAuth2UserInfo, socialType));

        // 사용자 속성 생성
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("id", user.getUserId());
        attributes.put("email", user.getEmail());

        // DefaultOAuth2User 객체 생성하여 반환
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "email"); // 기본 식별자 지정
    }

    /**
     * 사용자 생성 메서드
     *
     * @param oAuth2UserInfo
     * @param socialType
     * @return
     */
    private User createUser(OAuth2UserInfo oAuth2UserInfo, SocialType socialType) {
        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .socialId(oAuth2UserInfo.getId())
                .socialType(socialType)
                .build();
        return userRepository.save(user);
    }
}