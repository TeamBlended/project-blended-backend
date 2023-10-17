package com.gdsc.blended.user.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;

}

