package com.gdsc.blended.category.service;

import com.gdsc.blended.category.dto.CategoryDto;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAllCategory() {
        List<CategoryEntity> categorysList = categoryRepository.findAll();
        return categorysList.stream().map(CategoryEntity::toResponse).collect(Collectors.toList());
    }


    public CategoryEntity addCategory(CategoryDto createRequestDto) {
        return categoryRepository.save(createRequestDto.toEntity());
    }
}
