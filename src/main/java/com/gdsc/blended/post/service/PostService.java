package com.gdsc.blended.post.service;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.category.repository.CategoryRepository;
import com.gdsc.blended.post.dto.PostRequestDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public PostEntity findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post id"));
    }

    //전체 출력(Get)
    public List<PostResponseDto> getAllPost() {
        return postRepository.findAllDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    //게시글 생성 (Post)
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long categoryId , String email) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        PostEntity savedPost = postRepository.save(postRequestDto.toEntity(category,user));
        return new PostResponseDto(savedPost);
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

        if (!postEntity.getUserId().equals(user)) {
            throw new IllegalArgumentException("해당 게시글을 작성한 유저가 아닙니다.");
        }
        else {

            postEntity.setTitle(postRequestDto.getTitle());
            postEntity.setContent(postRequestDto.getContent());
            postEntity.setLocationName(postRequestDto.getLocationName());
            postEntity.setLatitude(postRequestDto.getLatitude());
            postEntity.setLongitude(postRequestDto.getLongitude());
            postEntity.setMaxRecruits(postRequestDto.getMaxRecruit());

            PostEntity updatedPost = postRepository.save(postEntity);

            return new PostResponseDto(updatedPost);
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

        if(!postEntity.getUserId().equals(user)) {
            postEntity.increaseViewCount(); // 조회수 증가
            postRepository.save(postEntity);
        }
        return new PostResponseDto(postEntity);
    }

}
