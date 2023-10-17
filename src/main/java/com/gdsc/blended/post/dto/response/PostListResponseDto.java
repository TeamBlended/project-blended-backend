package com.gdsc.blended.post.dto.response;

import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.dto.response.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostListResponseDto {
    private Long id;
    private Long alcoholId;
    private String title;
    private String content;
    private LocationDto shareLocation;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Date shareDateTime;
    private Long viewCount;
    private Long scrapCount;
    private Long maxParticipantsCount;
    private AuthorDto author;
    private String image;

    public PostListResponseDto(PostEntity post, String imagePathByPostId, Long alcoholId) {
        this.id = post.getId();
        this.alcoholId = alcoholId;
        this.title = post.getTitle();
        this.content = post.getContent();
        this.shareLocation = new LocationDto();
        this.shareLocation.setName(post.getLocationName());
        this.shareLocation.setLat(post.getLatitude());
        this.shareLocation.setLng(post.getLongitude());
        this.completed = post.getCompleted();
        this.createdAt = post.getCreatedDate();
        this.updatedAt = post.getModifiedDate();
        this.viewCount = post.getViewCount();
        this.scrapCount = post.getLikeCount();
        this.maxParticipantsCount = post.getMaxRecruits();
        this.shareDateTime = post.getShareDateTime();
        this.author = new AuthorDto(
                post.getUserId().getId(),
                post.getUserId().getNickname(),
                post.getUserId().getProfileImageUrl()
        );
        this.author.setNickname(post.getUserId().getNickname());
        this.author.setProfileImageUrl(post.getUserId().getProfileImageUrl());
        this.image = imagePathByPostId;
    }

}
