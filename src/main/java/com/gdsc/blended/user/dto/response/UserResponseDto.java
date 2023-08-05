package com.gdsc.blended.user.dto.response;

import com.gdsc.blended.user.entity.RoleType;
import com.gdsc.blended.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private RoleType roleType;

    public UserResponseDto(UserEntity userEntity){
        this.nickname = userEntity.getNickname();
        this.email = userEntity.getEmail();
        this.profileImageUrl = userEntity.getProfileImageUrl();
        this.roleType = RoleType.MEMBER;
    }
}
