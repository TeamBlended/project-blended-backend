package com.gdsc.blended.post.dto;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private Long maxRecruit;
    private String locationName;
    private Double latitude; //위도
    private Double longitude; //경도

    public PostEntity toEntity(CategoryEntity category, UserEntity user) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setLocationName(locationName);
        postEntity.setLatitude(latitude);
        postEntity.setLongitude(longitude);
        postEntity.setStatus(true);
        postEntity.setViewCount(0L);
        postEntity.setLikeCount(0L);
        postEntity.setRecruited(0L);
        postEntity.setMaxRecruits(maxRecruit);
        postEntity.setCategory(category);
        postEntity.setUserId(user);
        return postEntity;
    }
}
