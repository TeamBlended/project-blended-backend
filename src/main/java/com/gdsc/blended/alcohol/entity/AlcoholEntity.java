package com.gdsc.blended.alcohol.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_alcohol")
public class AlcoholEntity {
    @Id
    @Column(name = "alcohol_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "whisky_korean")
    private String whiskyKorean;

    @Column(name = "whisky_english")
    private String whiskyEnglish;

    private String abv;

    private String country;

    private String type;

    @Column(name = "alcohol_image_url",length = 1000)
    private String imgUrl;



}
