package com.gdsc.blended.user.entity;


import com.gdsc.blended.jwt.oauth.GoogleOAuth2UserInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;


//@DynamicInsert
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

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImageUrl;

    /*@Enumerated(EnumType.STRING)
    private SocialType socialType;*/

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private RoleType roleType;


    public UserEntity(GoogleOAuth2UserInfo userInfo){
        this.nickname = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.profileImageUrl = userInfo.getProfileImageUrl();
        this.roleType = RoleType.MEMBER;
    }

    public UserEntity(Long id){
        this.id = id;
    }

}

