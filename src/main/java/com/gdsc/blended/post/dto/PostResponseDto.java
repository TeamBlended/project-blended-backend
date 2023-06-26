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
    private Boolean status;
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
        this.shareLocation = new LocationDto();
        this.shareLocation.setName(postEntity.getLocationName());
        this.shareLocation.setLat(postEntity.getLatitude());
        this.shareLocation.setLng(postEntity.getLongitude());
        this.status = postEntity.getStatus();
        this.createdAt = postEntity.getCreatedDate();
        this.updatedAt = postEntity.getModifiedDate();
        this.viewCount = postEntity.getViewCount();
        this.scrapCount = postEntity.getLikeCount();
        this.maxParticipantsCount = postEntity.getMaxRecruits();
        this.shareDateTime = postEntity.getShareDateTime();
        this.category = postEntity.getCategory().getId();
        this.author = new AuthorDto();
        this.author.setNickname(postEntity.getUserId().getNickname());
        this.author.setEmail(postEntity.getUserId().getEmail());
        this.author.setProfileImageUrl(postEntity.getUserId().getProfileImageUrl());
    }
}
