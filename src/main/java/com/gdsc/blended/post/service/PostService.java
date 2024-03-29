package com.gdsc.blended.post.service;

import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import com.gdsc.blended.alcohol.repository.AlcoholRepository;
import com.gdsc.blended.category.repository.CategoryRepository;
import com.gdsc.blended.common.message.AlcoholResponseMessage;
import com.gdsc.blended.common.message.PostResponseMessage;
import com.gdsc.blended.common.message.UserResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.common.image.service.S3UploadService;
import com.gdsc.blended.common.image.service.ImageService;
import com.gdsc.blended.jwt.oauth.OAuth2UserInfo;
import com.gdsc.blended.post.dto.*;
import com.gdsc.blended.post.dto.request.PostRequestDto;
import com.gdsc.blended.post.dto.request.PostUpdateRequestDto;
import com.gdsc.blended.post.dto.response.*;
import com.gdsc.blended.post.entity.ExistenceStatus;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.entity.PostInAlcoholEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import com.gdsc.blended.post.heart.repository.HeartRepository;
import com.gdsc.blended.post.heart.service.HeartService;
import com.gdsc.blended.post.repository.PostInAlcoholRepository;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.dto.response.AuthorNicknameDto;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final HeartRepository heartRepository;
    private final HeartService heartService;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final S3UploadService s3UploadService;
    private final AlcoholRepository alcoholRepository;
    private final PostInAlcoholRepository postInAlcoholRepository;

    @Transactional
    //전체 출력(Get)
    public Page<PostListResponseDto> getAllPost(Pageable pageable) {
        List<PostListResponseDto> postResponseDtos = postRepository.findAll(pageable).stream()
                .filter(postDto -> postDto.getExistenceStatus() != ExistenceStatus.NON_EXIST)
                .map(postDto -> {
                    PostInAlcoholEntity alcoholEntity =postInAlcoholRepository.findByPostEntityId(postDto.getId());
                    return new PostListResponseDto(postDto, imageService.findImagePathByPostId(postDto.getId()),alcoholEntity.getAlcoholEntity().getId());
                })
            .collect(Collectors.toList());

        return new PageImpl<>(postResponseDtos, pageable, postResponseDtos.size());
    }

    //게시글 생성 (Post)
    @Transactional
    public PostCreateResponseDto createPost(PostRequestDto postRequestDto, String email) {
        /*CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));*/
        UserEntity user = findUserByEmail(email);
        ImageEntity image = findImagePath(postRequestDto.getImagePath());

        PostEntity postEntity = postRequestDto.toEntity(user);
        PostEntity savedPost = postRepository.save(postEntity);
        AlcoholEntity alcohol = alcoholRepository.findById(postRequestDto.getAlcoholId())
                .orElseThrow(() -> new ApiException(AlcoholResponseMessage.ALCOHOL_NOT_FOUND));

        PostInAlcoholEntity postInAlcoholEntity = PostInAlcoholEntity.builder()
                .postEntity(savedPost)
                .alcoholEntity(alcohol)
                .build();

        postInAlcoholRepository.save(postInAlcoholEntity);

        if (image != null) {
            image.setPost(postEntity);
        }

        return new PostCreateResponseDto(savedPost, imageService.findImagePathByPostId(savedPost.getId()), postInAlcoholEntity);
    }

    // 게시글 삭제(delete)
    @Transactional
    public void deletePostDB(Long postId,  String email) {
        PostEntity postEntity = checkPostOwnerShip(postId, email);

        ImageEntity image = imageService.findImageByPostId(postId);
        if (image != null) {
            s3UploadService.delete(image.getPath()); // S3이미지 삭제
            imageRepository.delete(image); // DB에서 이미지 삭제
        }

        // 게시물 삭제
        postRepository.delete(postEntity);
    }

    @Transactional
    public PostResponseDto deletePost(Long postId, String email) {
        PostEntity postEntity = checkPostOwnerShip(postId, email);
        postEntity.setExistenceStatus(ExistenceStatus.NON_EXIST);

        PostEntity deletedPost = postRepository.save(postEntity);
        return new PostResponseDto(deletedPost, imageService.findImagePathByPostId(deletedPost.getId()));
    }

    // 게시글 수정(Put)

    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto postRequestDto, String email) {

        PostEntity postEntity = checkPostOwnerShip(postId, email);
        String imageUrl = imageService.findImagePathByPostId(postId);
        postEntity.setTitle(postRequestDto.getTitle());
        postEntity.setContent(postRequestDto.getContent());
        postEntity.setLocationName(postRequestDto.getLocationName());
        postEntity.setLatitude(postRequestDto.getLatitude());
        postEntity.setLongitude(postRequestDto.getLongitude());
        PostEntity updatedPost = postRepository.save(postEntity);

        return new PostResponseDto(updatedPost, imageService.findImagePathByPostId(updatedPost.getId()));
    }

    @Transactional
    public PostDetailResponseDto detailPost(Long postId, String email) {
        UserEntity user = findUserByEmail(email);
        PostEntity postEntity = findPostByPostId(postId);
        PostInAlcoholEntity postInAlcoholEntity = findAlcoholId(postId);
        Optional<AlcoholEntity> alcohol = alcoholRepository.findById(postInAlcoholEntity.getAlcoholEntity().getId());

        AlcoholInfo alcoholInfo = new AlcoholInfo();
        alcoholInfo.setAlcohol_id(alcohol.get().getId());
        alcoholInfo.setWhiskyKorean(alcohol.get().getWhiskyKorean());
        alcoholInfo.setWhiskyEnglish(alcohol.get().getWhiskyEnglish());
        alcoholInfo.setImgUrl(alcohol.get().getImgUrl());
        alcoholInfo.setType(alcohol.get().getType());
        alcoholInfo.setCountry(alcohol.get().getCountry());
        alcoholInfo.setAbv(alcohol.get().getAbv());

        //image 찾아오기
        String imageUrl = imageService.findImagePathByPostId(postId);

        HeartEntity heartEntity = heartRepository.findByPostAndUser(postEntity, user);
        boolean heartCheck = heartEntity != null && heartEntity.getStatus();

        if (!postEntity.getUserId().getId().equals(user.getId())) {
            postEntity.increaseViewCount(); // 조회수 증가
            postRepository.save(postEntity);
        }

        return new PostDetailResponseDto(postEntity, heartCheck, imageUrl, alcoholInfo);
    }

    @Transactional
    public Page<GeoListResponseDto> getPostsByDistance(Double latitude, Double longitude , Integer page, Integer size) {

        if (latitude == null || longitude == null) {
            throw new ApiException(PostResponseMessage.NOT_FOUND_LATANDLONG);
        }
        Pageable pageable = PageRequest.of(page, size);
        List<PostEntity> postEntities = postRepository.findByCompletedFalse(pageable).getContent();

        List<GeoListResponseDto> postsByDistance = new ArrayList<>();
        for (PostEntity postEntity : postEntities) {
            Double postLatitude = postEntity.getLatitude();
            Double postLongitude = postEntity.getLongitude();

            // 위도 경도 간의 거리 계산 로직
            double distance = calculateDistance(latitude, longitude, postLatitude, postLongitude);
            if (distance <= 6) { // 단위는 km
                if (postEntity.getExistenceStatus() == ExistenceStatus.EXIST) {
                    PostInAlcoholEntity postInAlcohol = findAlcoholId(postEntity.getId());
                    ImageEntity image = imageRepository.findByPostId(postEntity.getId());

                    GeoListResponseDto postDto = new GeoListResponseDto();
                    postDto.setId(postEntity.getId());
                    postDto.setAlcoholId(postInAlcohol.getAlcoholEntity().getId());
                    postDto.setTitle(postEntity.getTitle());
                    postDto.setContent(postEntity.getContent());
                    postDto.setImage(image.getPath());
                    LocationDto locationDto = new LocationDto();
                    locationDto.setName(postEntity.getLocationName());
                    locationDto.setLng(postEntity.getLongitude());
                    locationDto.setLat(postEntity.getLatitude());
                    postDto.setShareDateTime(postEntity.getShareDateTime());
                    postDto.setShareLocation(locationDto);
                    postDto.setDistanceRange(distance);
                    postDto.setCompleted(postEntity.getCompleted());
                    postDto.setCreatedAt(postEntity.getCreatedDate());
                    postDto.setUpdatedAt(postEntity.getModifiedDate());
                    postDto.setMaxParticipantsCount(postEntity.getMaxRecruits());
                    AuthorDto authorDto = new AuthorDto();
                    authorDto.setId(postEntity.getUserId().getId());
                    authorDto.setNickname(postEntity.getUserId().getNickname());
                    authorDto.setProfileImageUrl(postEntity.getUserId().getProfileImageUrl());
                    postDto.setAuthor(authorDto);
                    postsByDistance.add(postDto);
                }
            }
        }
        postsByDistance.sort(Comparator.comparingDouble(GeoListResponseDto::getDistanceRange));

        return new PageImpl<>(postsByDistance);
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
    public Page<PostListResponseDto> getNewestPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PostEntity> postPage = postRepository.findAll(pageable);

        if (postPage.isEmpty()) {
            throw new ApiException(PostResponseMessage.POST_NOT_FOUND);
        }

        List<PostListResponseDto> validPosts = new ArrayList<>();
        for (PostEntity post : postPage) {
            if (post.getExistenceStatus() != ExistenceStatus.NON_EXIST) {
                PostInAlcoholEntity postInAlcohol = findAlcoholId(post.getId());
                validPosts.add(new PostListResponseDto(post, imageService.findImagePathByPostId(post.getId()), postInAlcohol.getAlcoholEntity().getId()));
            }
        }

        return new PageImpl<>(validPosts, pageable, validPosts.size());
    }

    @Transactional
    public Page<PostListResponseDto> getPostsSortedByHeart(Pageable pageable) {
        Page<PostEntity> postPage = postRepository.findAllByOrderByLikeCountDesc(pageable);
        if (postPage.isEmpty()) {
            throw new ApiException(PostResponseMessage.POST_NOT_FOUND);
        }
        List<PostListResponseDto> validPosts = new ArrayList<>();
        for (PostEntity post : postPage) {
            if (post.getExistenceStatus() != ExistenceStatus.NON_EXIST) {
                PostInAlcoholEntity postInAlcohol = findAlcoholId(post.getId());
                validPosts.add(new PostListResponseDto(post, imageService.findImagePathByPostId(post.getId()), postInAlcohol.getAlcoholEntity().getId()));
            }
        }
        return new PageImpl<>(validPosts, pageable, validPosts.size());
    }

    @Transactional
    public Page<SearchResponseDto> searchPosts(String keyword, Pageable pageable) {
        Page<PostEntity> findPosts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();

        for (PostEntity postEntity : findPosts) {
            if (postEntity.getExistenceStatus() != ExistenceStatus.NON_EXIST) {
                String image = imageService.findImagePathByPostId(postEntity.getId());
                SearchResponseDto searchResponseDto = SearchResponseDto.builder()
                        .id(postEntity.getId())
                        .title(postEntity.getTitle())
                        .content(postEntity.getContent())
                        .shareLocation(new LocationDto(postEntity.getLocationName(), postEntity.getLatitude(), postEntity.getLongitude()))
                        .author(new AuthorNicknameDto(postEntity.getUserId().getNickname()))
                        .shareDateTime(postEntity.getShareDateTime())
                        .createdAt(postEntity.getCreatedDate())
                        .maxParticipantsCount(postEntity.getMaxRecruits())
                        .image(image)
                        .build();
                searchResponseDtoList.add(searchResponseDto);
            }
        }

        if (searchResponseDtoList.isEmpty()) { // 검색 결과가 없는 경우
            throw new ApiException(PostResponseMessage.POST_NOT_FOUND);
        }

        return new PageImpl<>(searchResponseDtoList, pageable, findPosts.getTotalElements());
    }

    @Transactional
    public PostResponseDto completePost(Long postId, String email) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ApiException(PostResponseMessage.POST_NOT_FOUND));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserResponseMessage.USER_NOT_FOUND));

        if (!postEntity.getUserId().getId().equals(user.getId())) {
            throw new ApiException(UserResponseMessage.USER_NOT_MATCH);
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

    @Transactional
    public Page<PostListResponseDto> getMyPostList(String email) {
        UserEntity user = findUserByEmail(email);
        List<PostEntity> postEntities = postRepository.findByUserId(user);

        List<PostListResponseDto> validPosts = new ArrayList<>();
        for (PostEntity postEntity : postEntities) {
            if (postEntity.getExistenceStatus() != ExistenceStatus.NON_EXIST) {
                PostInAlcoholEntity postInAlcohol = findAlcoholId(postEntity.getId());
                validPosts.add(new PostListResponseDto(postEntity, imageService.findImagePathByPostId(postEntity.getId()), postInAlcohol.getAlcoholEntity().getId()));
            }
        }

        if (postEntities.isEmpty()) {
            throw new ApiException(PostResponseMessage.POST_NOT_FOUND);
        }


        return new PageImpl<>(validPosts);
    }

    public ImageEntity findImagePath(String path){
        return imageRepository.findByPath(path).orElseThrow(() ->
                new ApiException(PostResponseMessage.NOT_FOUND_IMAGE));
    }

    public PostEntity findPostByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new ApiException(PostResponseMessage.POST_NOT_FOUND));
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(UserResponseMessage.USER_NOT_FOUND));
    }
    private PostInAlcoholEntity findAlcoholId(Long postId) {
        return (PostInAlcoholEntity) postInAlcoholRepository.findByPostEntityId(postId);
    }

    private PostEntity checkPostOwnerShip(Long postId, String email){
        PostEntity postEntity = findPostByPostId(postId);
        UserEntity user = findUserByEmail(email);
        if (!postEntity.getUserId().getId().equals(user.getId())) {
            throw new ApiException(UserResponseMessage.USER_NOT_MATCH);
        }
        return postEntity;
    }

    private List<AlcoholEntity> findByAlcoholContaining(String keyword) {
        return alcoholRepository.findByWhiskyKoreanContainingOrWhiskyEnglishContainingIgnoreCase(keyword, keyword);
    }
}