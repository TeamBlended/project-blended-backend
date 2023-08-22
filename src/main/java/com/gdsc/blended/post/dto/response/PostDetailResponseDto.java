package com.gdsc.blended.post.dto.response;

import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.entity.PostInAlcoholEntity;
import com.gdsc.blended.user.dto.response.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocationDto shareLocation;
    private Boolean liked;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Date shareDateTime;
    private Long viewCount;
    private Long scrapCount;
    private Long maxParticipantsCount;
    private Long category;
    private AuthorDto author;
    private String image;
    private Long alcoholId;

    public PostDetailResponseDto(PostEntity postEntity, Boolean heartcheck, String imageUrl, PostInAlcoholEntity postInAlcohol){
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.shareLocation = new LocationDto();
        this.shareLocation.setName(postEntity.getLocationName());
        this.shareLocation.setLat(postEntity.getLatitude());
        this.shareLocation.setLng(postEntity.getLongitude());
        this.liked = heartcheck;
        this.completed = postEntity.getCompleted();
        this.createdAt = postEntity.getCreatedDate();
        this.updatedAt = postEntity.getModifiedDate();
        this.viewCount = postEntity.getViewCount();
        this.scrapCount = postEntity.getLikeCount();
        this.maxParticipantsCount = postEntity.getMaxRecruits();
        this.shareDateTime = postEntity.getShareDateTime();
        this.category = postEntity.getCategory().getId();
        this.author = new AuthorDto();
        this.author.setNickname(postEntity.getUserId().getNickname());
        this.author.setProfileImageUrl(postEntity.getUserId().getProfileImageUrl());
        this.image = imageUrl;
        this.alcoholId = postInAlcohol.getAlcoholEntity().getId();
    }
}
