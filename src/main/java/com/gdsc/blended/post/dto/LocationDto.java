package com.gdsc.blended.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
public class LocationDto {
    private Double longitude;
    private Double latitude;
    public LocationDto(Point entity){
        this.longitude = entity.getX();
        this.latitude = entity.getY();
    }
}
