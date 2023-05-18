package com.gdsc.blended.user.entity;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@RequiredArgsConstructor
@Entity@Table(name = "tb_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "user_profile_image")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private RoleType roleType;


    @Builder
    public UserEntity(Long id, String nickname, String email, String profileImageUrl, SocialType socialType , RoleType roleType){
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.socialType = socialType;
        this.roleType = roleType;
    }

    public UserEntity(Long id){
        this.id = id;
    }

}

