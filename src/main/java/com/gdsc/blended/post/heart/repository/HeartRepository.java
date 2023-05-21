package com.gdsc.blended.post.heart.repository;

import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import com.gdsc.blended.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<HeartEntity, Integer> {
    HeartEntity findByPostAndUser(PostEntity post, UserEntity user);
}

