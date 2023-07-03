package com.gdsc.blended.post.service;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.category.repository.CategoryRepository;
import com.gdsc.blended.common.image.dto.ImageDto;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.service.S3UploadService;
import com.gdsc.blended.common.image.service.ImageService;
import com.gdsc.blended.post.dto.GeoListResponseDto;
import com.gdsc.blended.post.dto.LocationDto;
import com.gdsc.blended.post.dto.PostRequestDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.dto.response.UserResponseDto;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final S3UploadService s3UploadService;

    public PostEntity findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post id"));
    }

    @Transactional
    //전체 출력(Get)
    public Page<PostResponseDto> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable).map(postDto ->
                new PostResponseDto(postDto, imageService.findImageByPostId(postDto.getId()))
        );
    }

    //게시글 생성 (Post)
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long categoryId, MultipartFile multipartFile, String email) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));


        String imageUrl = null;
        try {
            imageUrl = s3UploadService.upload(multipartFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("사진 S3 업로드 실패");
        }

        PostEntity postEntity = postRequestDto.toEntity(category, user);
        PostEntity savedPost = postRepository.save(postEntity);
        ImageEntity image = imageService.createImage(imageUrl, savedPost);

        return new PostResponseDto(savedPost, image.getPath());

    }

    // 게시글 삭제(delete)
    @Transactional
    public void deletePost(Long postId, String email) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if (!postEntity.getUserId().equals(user)) {
            throw new IllegalArgumentException("해당 게시글을 작성한 유저가 아닙니다.");
        } else {
            postRepository.delete(postEntity);
        }
    }


    //TODO .. 만약에 기존 모집인원이 4명이여서 3명 참가했는데 2명으로 수정한다면?,,,

    // 게시글 수정(Put)
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, String email) {

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));

        if (!postEntity.getUserId().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 게시글을 작성한 유저가 아닙니다.");
        } else {

            String imageUrl = imageService.findImageByPostId(postId);
            postEntity.setTitle(postRequestDto.getTitle());
            postEntity.setContent(postRequestDto.getContent());
            postEntity.setLocationName(postRequestDto.getLocationName());
            postEntity.setLatitude(postRequestDto.getLatitude());
            postEntity.setLongitude(postRequestDto.getLongitude());
            postEntity.setMaxRecruits(postRequestDto.getMaxRecruit());
            PostEntity updatedPost = postRepository.save(postEntity);

            return new PostResponseDto(updatedPost, imageUrl);
        }
    }

    @Transactional
    public PostResponseDto detailPost(Long postId, String email) {
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postId);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if (optionalPostEntity.isEmpty()) {
            return null;
        }
        PostEntity postEntity = optionalPostEntity.get();
        //image 찾아오기
        String imageUrl = imageService.findImageByPostId(postId);
        if(!postEntity.getUserId().getId().equals(user.getId())) {

            postEntity.increaseViewCount(); // 조회수 증가
            postRepository.save(postEntity);
        }
        return new PostResponseDto(postEntity,imageUrl);
    }

    @Transactional
    public List<GeoListResponseDto> getPostsByDistance(Double latitude, Double longitude, Double MAX_DISTANCE) {
        List<PostEntity> postEntities = postRepository.findByCompletedFalse();

        List<GeoListResponseDto> postsByDistance = new ArrayList<>();
        for (PostEntity postEntity : postEntities) {
            Double postLatitude = postEntity.getLatitude();
            Double postLongitude = postEntity.getLongitude();

            // 위도 경도 간의 거리 계산 로직
            double distance = calculateDistance(latitude, longitude, postLatitude, postLongitude);

            GeoListResponseDto postDto = new GeoListResponseDto();
            postDto.setId(postEntity.getId());
            postDto.setTitle(postEntity.getTitle());
            postDto.setContent(postEntity.getContent());
            LocationDto locationDto = new LocationDto();
            locationDto.setName(postEntity.getLocationName());
            locationDto.setLng(postEntity.getLongitude());
            locationDto.setLat(postEntity.getLatitude());
            postDto.setShareLocation(locationDto);
            postDto.setDistanceRange(distance);
            postDto.setCompleted(postEntity.getCompleted());
            postDto.setUpdatedAt(postEntity.getModifiedDate());
            postDto.setMaxParticipantsCount(postEntity.getMaxRecruits());
            AuthorDto authorDto = new AuthorDto();
            authorDto.setNickname(postEntity.getUserId().getNickname());
            authorDto.setProfileImageUrl(postEntity.getUserId().getProfileImageUrl());
            postDto.setAuthor(authorDto);

            if (distance <= MAX_DISTANCE) { // 단위는 km
                postsByDistance.add(postDto);
            }
        }
        postsByDistance.sort(Comparator.comparingDouble(GeoListResponseDto::getDistanceRange));

        return postsByDistance;
    }

    private double calculateDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        // 지구의 반지름 (단위: km)
        double radius = 6371;

        // 위도와 경도를 라디안 단위로 변환
        double lat1Rad = Math.toRadians(latitude1);
        double lon1Rad = Math.toRadians(longitude1);
        double lat2Rad = Math.toRadians(latitude2);
        double lon2Rad = Math.toRadians(longitude2);

        // 두 지점의 위도 차이와 경도 차이 계산
        double latDiff = lat2Rad - lat1Rad;
        double lonDiff = lon2Rad - lon1Rad;

        // Haversine formula를 사용하여 거리 계산
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radius * c;
    }

    @Transactional
    public Page<PostResponseDto> getNewestPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedDate").descending());
        return postRepository.findAll(pageable).map(post ->
            new PostResponseDto(post, imageService.findImageByPostId(post.getId()))
        );
    }

    @Transactional
    public Page<PostResponseDto> getPostsSortedByHeart(Pageable pageable) {
        return postRepository.findAllByOrderByLikeCountDesc(pageable).map(post ->
                new PostResponseDto(post, imageService.findImageByPostId(post.getId()))
        );
    }

    @Transactional
    public List<PostResponseDto> searchPosts(String keyword) {
        /*try{
            keyword = URLDecoder.decode(keyword, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ArrayList<>();
        }*/

        List<PostEntity> findPosts = postRepository.findByTitleOrContentContaining(keyword, keyword);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        if (findPosts.isEmpty()) {
            return postResponseDtoList;
        }

        for (PostEntity postEntity : findPosts) {
            PostResponseDto postResponseDto = PostResponseDto.builder()
                    .title(postEntity.getTitle())
                    .content(postEntity.getContent())
                    .build();
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }

    @Transactional
    public PostResponseDto completePost(Long postId, String email) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));

        if (!postEntity.getUserId().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 게시글을 작성한 유저가 아닙니다.");
        } else {
            postEntity.setCompleted(!postEntity.getCompleted());

            PostEntity updatedPost = postRepository.save(postEntity);

            return new PostResponseDto(updatedPost);
        }
    }

    @Transactional
    public void updateCompletedStatus() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<PostEntity> postEntities = postRepository.findAll();

        for (PostEntity postEntity : postEntities) {
            Date shareDateTime = postEntity.getShareDateTime();
            LocalDateTime localShareDateTime = shareDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (localShareDateTime.isBefore(currentDateTime)) {
                postEntity.setCompleted(true);
            }
        }
    }
}