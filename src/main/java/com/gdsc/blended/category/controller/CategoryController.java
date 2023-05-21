package com.gdsc.blended.category.controller;

import com.gdsc.blended.category.dto.CategoryDto;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "카테고리")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> readAll(){
        List<CategoryDto> categoryList = this.categoryService.findAllCategory();
        return ResponseEntity.ok(categoryList);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto createRequestDto){
        CategoryEntity result = categoryService.addCategory(createRequestDto);
        return (result != null ) ?
                ResponseEntity.status(HttpStatus.CREATED).body(result.toResponse()) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<String> getCategoryNameById(@PathVariable Long id) {
        String categoryName = categoryService.getCategoryNameById(id);
        return ResponseEntity.ok(categoryName);
    }
}
