package com.gdsc.blended.post.dto.request;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private Long maxParticipantsCount;
    //    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date shareDateTime;
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
        postEntity.setLiked(true);
        postEntity.setCompleted(false);
        postEntity.setViewCount(0L);
        postEntity.setLikeCount(0L);
        postEntity.setShareDateTime(shareDateTime);
        postEntity.setMaxRecruits(maxParticipantsCount);
        postEntity.setCategory(category);
        postEntity.setUserId(user);
        return postEntity;
    }
}
