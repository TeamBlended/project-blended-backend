package com.gdsc.blended.user.entity;


import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
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

    private String location;



    @Builder
    public User(Long userId, String name, String email, String password, UserType type){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.type = type;
        this.location = location;
    }

    public User(Long userId){
        this.userId = userId;
    }

}
