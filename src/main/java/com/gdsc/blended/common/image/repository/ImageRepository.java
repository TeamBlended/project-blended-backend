package com.gdsc.blended.common.image.repository;


import com.gdsc.blended.common.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByPath(String path);
    Optional<ImageEntity> findById(Long imageId);

    ImageEntity findByPostId(Long postId);
}
