package com.gdsc.blended.category.service;

import com.gdsc.blended.category.dto.CategoryDto;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        return categoryRepository.save(CategoryEntity.builder()
                .name(createRequestDto.getName())
                .build());
    }


    public String getCategoryNameById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
        return categoryEntity.getName();
    }
}
