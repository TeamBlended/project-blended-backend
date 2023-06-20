package com.gdsc.blended.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDto {
    private String nickname;
    private String email;
    private String profileImageUrl;
}

