package com.gdsc.blended.post.dto.response;

import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.dto.response.AuthorNicknameDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocationDto shareLocation;
    private Date shareDateTime;
    private Long maxParticipantsCount;
    private AuthorNicknameDto author;
    private LocalDateTime createdAt;
    private String image;


}
