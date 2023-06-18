package com.gdsc.blended.alcohol.service;

import com.gdsc.blended.alcohol.dto.AlcoholDto;
import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import com.gdsc.blended.alcohol.repository.AlcoholRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlcoholService {
    private final AlcoholRepository alcoholRepository;


    @Transactional
    public List<AlcoholDto> searchAlcohols(String keyword) {
        List<AlcoholEntity> findAlcohols = alcoholRepository.findByWhiskyKoreanContaining(keyword);
        List<AlcoholDto> alcoholDtoList = new ArrayList<>();

        if (findAlcohols.isEmpty()) return alcoholDtoList;

        for (AlcoholEntity alcoholEntity : findAlcohols) {
            alcoholDtoList.add(this.convertEntityToDto(alcoholEntity));
        }
        return alcoholDtoList;
    }

    private AlcoholDto convertEntityToDto(AlcoholEntity alcoholEntity) {
        return AlcoholDto.builder()
                .id(alcoholEntity.getId())
                .whiskyKorean(alcoholEntity.getWhiskyKorean())
                .whiskyEnglish(alcoholEntity.getWhiskyEnglish())
                .abv(alcoholEntity.getAbv())
                .country(alcoholEntity.getCountry())
                .type(alcoholEntity.getType())
                .imgUrl(alcoholEntity.getImgUrl())
                .build();
    }
}

