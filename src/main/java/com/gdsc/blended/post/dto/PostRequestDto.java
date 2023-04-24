package com.gdsc.blended.post.dto;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private Long maxRecruit;

    public PostEntity toEntity(CategoryEntity category) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setStatus(true);
        postEntity.setViewCount(0L);
        postEntity.setScrapCount(0L);
        postEntity.setRecruited(0L);
        postEntity.setMaxRecruits(maxRecruit);
        postEntity.setCategory(category);
        return postEntity;
    }
}
