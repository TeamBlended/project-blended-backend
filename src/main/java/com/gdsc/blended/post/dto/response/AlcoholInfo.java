package com.gdsc.blended.post.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AlcoholInfo {
    private Long alcohol_id;
    private String whiskyKorean;
    private String whiskyEnglish;
    private String abv;
    private String country;
    private String type;
    private String imgUrl;
}
