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
    private Long userId;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    private String role;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    private String provider;

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
