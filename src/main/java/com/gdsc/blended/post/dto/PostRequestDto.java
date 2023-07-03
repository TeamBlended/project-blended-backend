package com.gdsc.blended.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private Long maxRecruit;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date shareDateTime;
    private String locationName;
    private Double latitude; //위도
    private Double longitude; //경도
    private MultipartFile multipartFile;

    public PostEntity toEntity(CategoryEntity category, UserEntity user) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setLocationName(locationName);
        postEntity.setLatitude(latitude);
        postEntity.setLongitude(longitude);
        postEntity.setLiked(true);
        postEntity.setCompleted(false);
        postEntity.setViewCount(0L);
        postEntity.setLikeCount(0L);
        postEntity.setShareDateTime(shareDateTime);
        postEntity.setMaxRecruits(maxRecruit);
        postEntity.setCategory(category);
        postEntity.setUserId(user);
        return postEntity;
    }
}
