package com.gdsc.blended.post.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LocationDto {
    private String name;
    private Double lng;
    private Double lat;
}
