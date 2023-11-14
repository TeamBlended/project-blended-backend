package com.gdsc.blended.post.dto.request;

import com.gdsc.blended.post.entity.ExistenceStatus;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String title;
    @Nullable
    private String content;
    private Long maxParticipantsCount;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date shareDateTime;
    private String locationName;
    private Double latitude; //위도
    private Double longitude; //경도
    private String imagePath;
    private Long AlcoholId;

    public PostEntity toEntity(UserEntity user) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setLocationName(locationName);
        postEntity.setLatitude(latitude);
        postEntity.setLongitude(longitude);
        postEntity.setLiked(false);
        postEntity.setCompleted(false);
        postEntity.setViewCount(0L);
        postEntity.setLikeCount(0L);
        postEntity.setShareDateTime(shareDateTime);
        postEntity.setMaxRecruits(maxParticipantsCount);
        postEntity.setExistenceStatus(ExistenceStatus.EXIST);
        //postEntity.setCategory(category);
        postEntity.setUserId(user);
        return postEntity;
    }
}
