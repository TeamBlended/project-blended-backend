package com.gdsc.blended.category.entity;

import com.gdsc.blended.category.dto.CategoryDto;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "POST_CATEGORY")
@Builder
public class CategoryEntity {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;


    @Builder
    public CategoryEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDto toResponse() {
        return new CategoryDto(id, name);
    }


}
