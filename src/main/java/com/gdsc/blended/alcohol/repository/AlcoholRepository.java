package com.gdsc.blended.alcohol.repository;

import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AlcoholRepository extends JpaRepository<AlcoholEntity, Long> {
    // TODO: 2023/08/10 영어 같은경우는 대소문자 구별을 못하는 문제 있음 알고리즘 수정해야함 ㅠㅠ
    List<AlcoholEntity> findByWhiskyKoreanContainingOrWhiskyEnglishContaining(String koreanKeyword, String englishKeyword);
    Optional<AlcoholEntity> findByWhiskyKoreanOrWhiskyEnglish(String koreanKeyword , String englishKeyword);
}
