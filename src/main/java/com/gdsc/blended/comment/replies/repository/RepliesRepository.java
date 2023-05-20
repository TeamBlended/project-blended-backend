package com.gdsc.blended.comment.replies.repository;

import com.gdsc.blended.comment.replies.entity.RepliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepliesRepository extends JpaRepository<RepliesEntity, Long> {
    @Query("SELECT p FROM RepliesEntity p ORDER BY p.id DESC")
    List<RepliesEntity> findAllDesc();

    List<RepliesEntity> findByCommentId(Long postId);
}