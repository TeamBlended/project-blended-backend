package com.gdsc.blended.utils;

import org.springframework.data.domain.Page;

public class PagingUtil {

    public static <T> PagingResponse<T> toResponse(Page<T> page) {
        Long nextPage = page.isLast() ? null : (long) page.getNumber() + 2;
        return new PagingResponse<>(
                page.getContent(),
                page.getTotalElements(),
                nextPage
        );
    }

}
