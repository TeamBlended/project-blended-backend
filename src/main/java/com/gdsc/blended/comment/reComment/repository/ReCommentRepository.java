package com.gdsc.blended.comment.reComment.repository;

import com.gdsc.blended.comment.reComment.entity.ReCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReCommentRepository extends JpaRepository<ReCommentEntity, Long> {
    @Query("SELECT p FROM ReCommentEntity p ORDER BY p.id DESC")
    List<ReCommentEntity> findAllDesc();

    List<ReCommentEntity> findByCommentId(Long postId);
}