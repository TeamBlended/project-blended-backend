package com.gdsc.blended.comment.repository;

import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT p FROM CommentEntity p ORDER BY p.id DESC")
    List<CommentEntity>findAllDesc();

    List<CommentEntity> findByPostId(Long postId);

    //List<CommentEntity> findByUserAndCreatedAtBefore(UserEntity userEntity, LocalDateTime cutoffDate);

    List<CommentEntity> findByUserAndCreatedDateBefore(UserEntity userEntity, LocalDateTime cutoffDate);

}