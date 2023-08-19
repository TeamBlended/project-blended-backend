package com.gdsc.blended.alcohol.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class AlcoholCameraResponseDto {
    private Long id;
    private String whiskyKorean;
    private String whiskyEnglish;
    private String abv;
    private String country;
    private String type;
    private String imgUrl;
}
