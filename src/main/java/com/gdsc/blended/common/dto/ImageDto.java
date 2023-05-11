package com.gdsc.blended.common.dto;

import com.gdsc.blended.common.image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class ImageDto {
    //@Length(max = 500)
    private String image;

    public ImageDto(Image image) {
        this.image = image.getPath();
    }
}
