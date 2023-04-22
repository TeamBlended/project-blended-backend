package com.gdsc.blended.category.entity;

import com.gdsc.blended.category.dto.CategoryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "POST_CATEGORY")
public class CategoryEntity {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;


    @Builder
    public CategoryEntity( String name) {
        this.name = name;
    }

    public CategoryDto toResponse() {
        return new CategoryDto(id, name);
    }


}
