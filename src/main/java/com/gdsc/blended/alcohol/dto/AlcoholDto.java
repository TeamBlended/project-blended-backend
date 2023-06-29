/*
package com.gdsc.blended.alcohol.dto;

import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class AlcoholDto {
    private Long id;
    private String whiskyKorean;
    private String whiskyEnglish;
    private String abv;
    private String country;
    private String type;
    private String imgUrl;

    public AlcoholEntity toEntity(){
        return AlcoholEntity.builder()
                .id(id)
                .whiskyKorean(whiskyKorean)
                .whiskyEnglish(whiskyEnglish)
                .abv(abv)
                .country(country)
                .type(type)
                .imgUrl(imgUrl)
                .build();
    }
}
*/
