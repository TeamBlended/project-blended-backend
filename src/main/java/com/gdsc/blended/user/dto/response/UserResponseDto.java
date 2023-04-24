package com.gdsc.blended.user.dto.response;

import com.gdsc.blended.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long userID;
    private String name;
    private String email;

    public static UserResponseDto of(User user){
        return new UserResponseDto(user.getUserId(), user.getName(), user.getEmail());
    }
}
