package com.gdsc.blended.post.repository;

import com.gdsc.blended.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p ORDER BY p.id DESC")
    List<PostEntity> findAllDesc();

}
