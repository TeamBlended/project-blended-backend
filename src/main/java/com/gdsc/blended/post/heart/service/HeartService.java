package com.gdsc.blended.post.heart.service;

import com.gdsc.blended.common.apiResponse.PostResponseMessage;
import com.gdsc.blended.common.apiResponse.UserResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.common.image.service.ImageService;
import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import com.gdsc.blended.post.heart.repository.HeartRepository;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;


    public String likeBoard(Long id,String userEmail) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new ApiException(PostResponseMessage.POST_NOT_FOUND));
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ApiException(UserResponseMessage.USER_NOT_FOUND));
        if(heartRepository.findByPostAndUser(post,user) == null) {
            // 좋아요를 누른적 없다면 LikeBoard 생성 후, 좋아요 처리
            if(post.getLikeCount() == null){
                post.setLikeCount(0L);
            }
            post.setLikeCount(post.getLikeCount() + 1);
            HeartEntity heartEntity = new HeartEntity(post, user); // true 처리
            postRepository.save(post);
            heartRepository.save(heartEntity);
            return "좋아요 +1 완료";
        } else {
            HeartEntity heartEntity = heartRepository.findByPostAndUser(post, user);
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            heartRepository.delete(heartEntity);
            return "좋아요 취소";
        }
    }

    public Page<PostResponseDto> getMyHeartList(int page, int size, String userEmail) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postPage = heartRepository.findLikedPostsByUserEmail(pageable, userEmail);
        return postPage.map(this::mapToPostResponseDto);

    }

    private PostResponseDto mapToPostResponseDto(PostEntity postEntity) {
        String image = imageService.findImagePathByPostId(postEntity.getId());
        return PostResponseDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .shareLocation(LocationDto.builder()
                        .name(postEntity.getLocationName())
                        .lat(postEntity.getLatitude())
                        .lng(postEntity.getLongitude())
                        .build())
                .liked(postEntity.getLiked())
                .completed(postEntity.getCompleted())
                .createdAt(postEntity.getCreatedDate())
                .updatedAt(postEntity.getModifiedDate())
                .viewCount(postEntity.getViewCount())
                .scrapCount(postEntity.getLikeCount())
                .maxParticipantsCount(postEntity.getMaxRecruits())
                .shareDateTime(postEntity.getShareDateTime())
                .category(postEntity.getCategory().getId())
                .author(AuthorDto.builder()
                        .nickname(postEntity.getUserId().getNickname())
                        .profileImageUrl(postEntity.getUserId().getProfileImageUrl())
                        .build())
                .image(image)
                .build();
    }
}