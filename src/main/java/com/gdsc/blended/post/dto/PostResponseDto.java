package com.gdsc.blended.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.AllArgsConstructor;
import com.gdsc.blended.user.dto.response.AuthorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocationDto shareLocation;
    /*
        private String locationName;
        private Double latitude;
        private Double longitude;
    */
    private Boolean liked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Date shareDateTime;
    private Long viewCount;
    private Long scrapCount;
    private Long maxParticipantsCount;
    private Long category;
    private AuthorDto author;

    public PostResponseDto(PostEntity postEntity) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        shareLocation.setName(postEntity.getLocationName());
        shareLocation.setLat(postEntity.getLatitude());
        shareLocation.setLng(postEntity.getLongitude());
        this.liked = postEntity.getLiked();
        this.createdAt = postEntity.getCreatedDate();
        this.updatedAt = postEntity.getModifiedDate();
        this.viewCount = postEntity.getViewCount();
        this.scrapCount = postEntity.getLikeCount();
        this.maxParticipantsCount = postEntity.getMaxRecruits();
        this.shareDateTime = postEntity.getShareDateTime();
        this.category = postEntity.getCategory().getId();
        author.setNickname(postEntity.getUserId().getNickname());
        author.setEmail(postEntity.getUserId().getEmail());
        author.setProfileImageUrl(postEntity.getUserId().getProfileImageUrl());
    }
}
