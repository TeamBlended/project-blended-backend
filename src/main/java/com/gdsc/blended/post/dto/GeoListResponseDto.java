package com.gdsc.blended.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GeoListResponseDto {
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
    private double distance;
    



}
