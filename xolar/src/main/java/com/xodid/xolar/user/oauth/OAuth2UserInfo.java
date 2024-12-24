package com.xodid.xolar.user.oauth;

import java.util.Map;

/**
 * 소셜 로그인에서 공통적으로 사용하는 사용자 정보 추상 클래스
 *
 * 공통적으로 필요한 메서드(ID, 닉네임, 이메일)을 가져오는 메서드를 정의하고 있으며,
 * 소셜 로그인별로 다르게 구현할 수 있도록 추상 메서드로 선언
 */
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    // 소셜 로그인별로(구글, 카카오) 서로 다른 방식으로 사용자 정보를 제공하므로 추상 클래스 메서드로 선언
    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
}

