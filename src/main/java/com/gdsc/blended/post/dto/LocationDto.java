package com.gdsc.blended.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
public class LocationDto {
    private String name;
    private Double lng;
    private Double lat;
}
