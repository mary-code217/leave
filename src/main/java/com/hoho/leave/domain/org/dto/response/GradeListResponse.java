package com.hoho.leave.domain.org.dto.response;

import lombok.Data;

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

    public static GradeListResponse of(Integer page, Integer size,
                                        List<GradeDetailResponse> grades, Integer totalPage, Long totalElement,
                                        Boolean firstPage, Boolean lastPage) {
        GradeListResponse response = new GradeListResponse();

        response.page = page;
        response.size = size;
        response.grades = grades;
        response.totalPage = totalPage;
        response.totalElement = totalElement;
        response.firstPage = firstPage;
        response.lastPage = lastPage;

        return response;
    }
}
