package com.gdsc.blended.category.dto;

import com.gdsc.blended.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;

    public CategoryEntity toEntity() {
        return new CategoryEntity(name);
    }
}
