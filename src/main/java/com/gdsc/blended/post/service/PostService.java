package com.gdsc.blended.post.service;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.category.repository.CategoryRepository;
import com.gdsc.blended.post.dto.PostRequestDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
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
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long categoryId ) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));
        PostEntity savedPost = postRepository.save(postRequestDto.toEntity(category));
        return new PostResponseDto(savedPost);
    }

    // 게시글 삭제(delete)
    @Transactional
    public void deletePost(Long postId) {
        PostEntity postEntity = findById(postId);
        postRepository.delete(postEntity);
    }


    //TODO .. 만약에 기존 모집인원이 4명이여서 3명 참가했는데 2명으로 수정한다면?,,,

    // 게시글 수정(Put)
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto) {
        PostEntity postEntity = findById(postId);

        postEntity.setTitle(postRequestDto.getTitle());
        postEntity.setContent(postRequestDto.getContent());
        postEntity.setLocationName(postRequestDto.getLocationName());
        postEntity.setLatitude(postRequestDto.getLatitude());
        postEntity.setLongitude(postRequestDto.getLongitude());
        postEntity.setMaxRecruits(postRequestDto.getMaxRecruit());

        PostEntity updatedPost = postRepository.save(postEntity);

        return new PostResponseDto(updatedPost);
    }

    @Transactional
    public PostResponseDto detailPost(Long postId) {
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postId);
        if (optionalPostEntity.isEmpty()) {
            return null;
        }
        PostEntity postEntity = optionalPostEntity.get();

        return new PostResponseDto(postEntity);
    }

}
