package com.gdsc.blended.alcohol.controller;


import com.gdsc.blended.alcohol.dto.AlcoholDto;
import com.gdsc.blended.alcohol.service.AlcoholService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alcohols")
public class AlcoholController {
    private final AlcoholService alcoholService;


    @GetMapping("/{keyword}")
    public ResponseEntity<List<AlcoholDto>> searchAlcohols(@PathVariable String keyword){
        List<AlcoholDto> alcoholDtoList = alcoholService.searchAlcohols(keyword);
        return ResponseEntity.ok(alcoholDtoList);
    }
}
