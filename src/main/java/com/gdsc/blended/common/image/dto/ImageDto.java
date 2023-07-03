package com.gdsc.blended.common.image.dto;


import com.gdsc.blended.common.image.entity.ImageEntity;
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
    private String path;

    public ImageDto(ImageEntity image) {
        this.path = image.getPath();
    }
}
