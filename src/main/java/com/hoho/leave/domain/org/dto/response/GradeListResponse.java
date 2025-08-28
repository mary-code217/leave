package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GradeListResponse {
    Integer page;
    Integer size;
    List<GradeDetailResponse> grades;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static  GradeListResponse from(Integer page, Integer size,
                                          List<GradeDetailResponse> grades, Integer totalPage, Long totalElement,
                                          Boolean firstPage, Boolean lastPage) {
        return new GradeListResponse(page, size, grades, totalPage, totalElement, firstPage, lastPage);
    }
}
