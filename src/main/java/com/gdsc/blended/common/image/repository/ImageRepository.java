package com.gdsc.blended.common.image.repository;


import com.gdsc.blended.common.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByPath(String path);
}
