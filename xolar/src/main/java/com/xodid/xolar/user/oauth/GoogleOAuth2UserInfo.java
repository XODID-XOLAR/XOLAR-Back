package com.xodid.xolar.user.oauth;

import java.util.Map;

/**
 * 구글에서 제공하는 사용자 정보를 다룸
 * OAuth2UserInfo를 확장해 구글의 사용자 정보 구조에 맞춰 구현
 */
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
