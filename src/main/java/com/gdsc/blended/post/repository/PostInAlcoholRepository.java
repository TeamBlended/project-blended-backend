package com.gdsc.blended.post.repository;

import com.gdsc.blended.post.entity.PostInAlcoholEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostInAlcoholRepository extends JpaRepository<PostInAlcoholEntity, Long> {

    Optional<Object> findByPostEntityId(Long postId);
}
