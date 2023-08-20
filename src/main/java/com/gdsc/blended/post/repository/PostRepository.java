package com.gdsc.blended.post.repository;

import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p ORDER BY p.id DESC")
    List<PostEntity> findAllDesc();

    Page<PostEntity> findAllByOrderByLikeCountDesc(Pageable pageable);

    List<PostEntity> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);


    Page<PostEntity> findByCompletedFalse(Pageable pageable);


    List<PostEntity> findByUserId(UserEntity user);
}
