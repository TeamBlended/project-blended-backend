package com.gdsc.blended.user.dto.response;

import lombok.Getter;

@Getter
public class AuthorNicknameDto {
    private String nickname;

    public AuthorNicknameDto(String nickname) {
        this.nickname = nickname;
    }
}
