package com.gdsc.blended.user.entity;


import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", unique = true)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    private String role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_type")
    private UserType type;

    private String provider;

    @Column(name = "user_profile_image")
    private String profileImage;

    //private String location;
    

    @Builder
    public User(Long userId, String name, String email, UserType type){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public User(Long userId){
        this.userId = userId;
    }

}
