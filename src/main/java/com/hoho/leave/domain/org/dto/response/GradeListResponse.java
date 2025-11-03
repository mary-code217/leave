package com.hoho.leave.domain.org.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class GradeListResponse {
    Integer page;

    Integer size;

    List<GradeDetailResponse> grades;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    public static GradeListResponse of(Page<?> page, List<GradeDetailResponse> grades) {
        GradeListResponse response = new GradeListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.grades = grades;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
