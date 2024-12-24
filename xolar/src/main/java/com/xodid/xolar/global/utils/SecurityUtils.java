package com.xodid.xolar.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    /**
     * 현재 인증된 사용자 email 반환 메서드
     * 만약 인증 정보가 없는 경우, null 반환
     */
    public static String getCurrentUserEmail() {
        // SecurityContext에서 현재 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증정보가 없거나 사용자 이메일이 없는 경우, null 반환
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return authentication.getName();
    }
}
}
