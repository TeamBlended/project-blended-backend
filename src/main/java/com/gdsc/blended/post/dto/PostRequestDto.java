package com.gdsc.blended.post.dto;

import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private Long maxRecruit;
    private String locationName;
    private Double longitude;
    private Double latitude;

    public PostEntity toEntity(CategoryEntity category) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setLocationName(locationName);
        Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(longitude, latitude));
        postEntity.setLocation(point);
        postEntity.setStatus(true);
        postEntity.setViewCount(0L);
        postEntity.setScrapCount(0L);
        postEntity.setRecruited(0L);
        postEntity.setMaxRecruits(maxRecruit);
        postEntity.setCategory(category);
        return postEntity;
    }
}
