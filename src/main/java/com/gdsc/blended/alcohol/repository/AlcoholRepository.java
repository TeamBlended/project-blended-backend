package com.gdsc.blended.alcohol.repository;

import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AlcoholRepository extends JpaRepository<AlcoholEntity, Long> {
    List<AlcoholEntity> findByWhiskyKoreanContainingOrWhiskyEnglishContainingIgnoreCase(String koreanKeyword, String englishKeyword);
}
