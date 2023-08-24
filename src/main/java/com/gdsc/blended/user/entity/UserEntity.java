package com.gdsc.blended.user.entity;


import com.gdsc.blended.jwt.oauth.GoogleOAuth2UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "tb_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImageUrl;

    /*@Enumerated(EnumType.STRING)
    private SocialType socialType;*/

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private RoleType roleType;

    @Column(name = "withdrawal_date")
    private LocalDateTime withdrawalDate;


    public UserEntity(GoogleOAuth2UserInfo userInfo){
        this.name = userInfo.getName();
        this.email = userInfo.getEmail();
        this.profileImageUrl = userInfo.getProfileImageUrl();
        this.roleType = RoleType.MEMBER;
    }

    public UserEntity(Long id){
        this.id = id;
    }

}

