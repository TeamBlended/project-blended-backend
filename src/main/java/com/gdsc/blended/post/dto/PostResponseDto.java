package com.gdsc.blended.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    @JsonProperty("post_id")
    private Long id;
    private String title;
    private String content;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long viewCount;
    private Long scrapCount;
    private Long maxRecruits;
    private Long recruited;
    private Long category;

    public PostResponseDto(PostEntity postEntity) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.locationName = postEntity.getLocationName();
        this.latitude = postEntity.getLatitude();
        this.longitude = postEntity.getLongitude();
        this.status = postEntity.getStatus();
        this.createdAt = postEntity.getCreatedDate();
        this.updatedAt = postEntity.getModifiedDate();
        this.viewCount = postEntity.getViewCount();
        this.scrapCount = postEntity.getLikeCount();
        this.maxRecruits = postEntity.getMaxRecruits();
        this.recruited = postEntity.getRecruited();
        this.category = postEntity.getCategory().getId();
    }


}
