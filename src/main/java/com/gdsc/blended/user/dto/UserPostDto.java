package com.gdsc.blended.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPostDto {
    private String emaill;
    private String name;
    //지역, 프로필이미지 추가

    @Builder
    public UserPostDto(String email, String name){
        this.emaill = email;
        this.name = name;
    }
}
