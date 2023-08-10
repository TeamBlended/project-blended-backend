package com.gdsc.blended.alcohol.repository;

import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AlcoholRepository extends JpaRepository<AlcoholEntity, Long> {
    List<AlcoholEntity> findByWhiskyKoreanContainingOrWhiskyEnglishContaining(String koreanKeyword, String englishKeyword);
    Optional<AlcoholEntity> findByWhiskyKoreanOrWhiskyEnglish(String koreanKeyword , String englishKeyword);
}
