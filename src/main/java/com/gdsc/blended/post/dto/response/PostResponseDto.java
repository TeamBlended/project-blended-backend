package com.gdsc.blended.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.common.image.dto.ImageDto;
import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.entity.PostInAlcoholEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import jdk.jshell.Snippet;
import lombok.*;
import com.gdsc.blended.user.dto.response.AuthorDto;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
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
    private Long category;
    private AuthorDto author;
    private String image;

    public PostResponseDto(PostEntity postEntity, String image) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.shareLocation = new LocationDto();
        this.shareLocation.setName(postEntity.getLocationName());
        this.shareLocation.setLat(postEntity.getLatitude());
        this.shareLocation.setLng(postEntity.getLongitude());
        this.completed = postEntity.getCompleted();
        this.createdAt = postEntity.getCreatedDate();
        this.updatedAt = postEntity.getModifiedDate();
        this.viewCount = postEntity.getViewCount();
        this.scrapCount = postEntity.getLikeCount();
        this.maxParticipantsCount = postEntity.getMaxRecruits();
        this.shareDateTime = postEntity.getShareDateTime();
        //this.category = postEntity.getCategory().getId();
        this.author = new AuthorDto();
        this.author.setId(postEntity.getUserId().getId());
        this.author.setNickname(postEntity.getUserId().getNickname());
        this.author.setProfileImageUrl(postEntity.getUserId().getProfileImageUrl());
        this.image = image;
    }


    public PostResponseDto(PostEntity updatedPost) {
        this.id = updatedPost.getId();
        this.title = updatedPost.getTitle();
        this.content = updatedPost.getContent();
        this.shareLocation = new LocationDto();
        this.shareLocation.setName(updatedPost.getLocationName());
        this.shareLocation.setLat(updatedPost.getLatitude());
        this.shareLocation.setLng(updatedPost.getLongitude());
        this.completed = updatedPost.getCompleted();
        this.createdAt = updatedPost.getCreatedDate();
        this.updatedAt = updatedPost.getModifiedDate();
        this.viewCount = updatedPost.getViewCount();
        this.scrapCount = updatedPost.getLikeCount();
        this.maxParticipantsCount = updatedPost.getMaxRecruits();
        this.shareDateTime = updatedPost.getShareDateTime();
        //this.category = updatedPost.getCategory().getId();
        this.author = new AuthorDto();
        this.author.setNickname(updatedPost.getUserId().getNickname());
        this.author.setProfileImageUrl(updatedPost.getUserId().getProfileImageUrl());
    }

    public PostResponseDto(PostEntity post, String imagePathByPostId, Long Alcoholid) {
        this.id = post.getId();
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
        //this.category = post.getCategory().getId();
        this.author = new AuthorDto();
        this.author.setNickname(post.getUserId().getNickname());
        this.author.setProfileImageUrl(post.getUserId().getProfileImageUrl());
        this.image = imagePathByPostId;
    }
}
