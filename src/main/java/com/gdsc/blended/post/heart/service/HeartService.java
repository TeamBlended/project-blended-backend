package com.gdsc.blended.post.heart.service;

import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import com.gdsc.blended.post.heart.repository.HeartRepository;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public String likeBoard(Long id,Long userId) {
        PostEntity post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다."));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
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
}