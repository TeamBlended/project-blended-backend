package com.gdsc.blended.category.dto;

import com.gdsc.blended.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;


    public CategoryDto(List<CategoryDto> categoryHolder) {
    }


    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .id(id)
                .name(name)
                .build();
    }
}
