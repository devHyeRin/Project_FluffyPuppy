package com.fluffypuppy.shop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // 스프링 시큐리티는 기본적으로 "ROLE_" 접두사를 사용합니다.
    USER("ROLE_USER", "일반사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String title;
}