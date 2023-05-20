package com.gdsc.blended.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ADMIN("어드민"),
    MEMBER("유저");

    private final String value;
}
