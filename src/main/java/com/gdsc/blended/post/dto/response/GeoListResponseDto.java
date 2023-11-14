package com.gdsc.blended.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.user.dto.response.AuthorDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class GeoListResponseDto {
    private Long id;
    private Long alcoholId;
    private String image;
    private String title;
    private String content;
    private LocationDto shareLocation;
    private Date shareDateTime;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long maxParticipantsCount;
    private AuthorDto author;
    private Double distanceRange;


}
