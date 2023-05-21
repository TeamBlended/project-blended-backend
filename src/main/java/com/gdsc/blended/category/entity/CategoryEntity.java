package com.gdsc.blended.category.entity;

import com.gdsc.blended.category.dto.CategoryDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "tb_category")
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
