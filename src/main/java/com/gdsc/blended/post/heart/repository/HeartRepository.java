package com.gdsc.blended.post.heart.repository;

import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import com.gdsc.blended.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HeartRepository extends JpaRepository<HeartEntity, Integer> {
    HeartEntity findByPostAndUser(PostEntity post, UserEntity user);

    @Query("SELECT h.post FROM HeartEntity h WHERE h.user.email = :userEmail")
    Page<PostEntity> findLikedPostsByUserEmail(Pageable pageable, String userEmail);
}

