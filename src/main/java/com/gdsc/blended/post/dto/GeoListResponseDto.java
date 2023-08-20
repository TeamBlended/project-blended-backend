package com.gdsc.blended.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.user.dto.response.AuthorDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GeoListResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocationDto shareLocation;
    private Boolean completed;
    private LocalDateTime updatedAt;
    private Long maxParticipantsCount;
    private AuthorDto author;
    private Double distanceRange;


}
