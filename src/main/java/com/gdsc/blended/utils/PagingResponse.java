package com.gdsc.blended.utils;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse<T> {

    private List<T> data;
    private Long totalCount;
    private Long nextPage;

}