package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GradeDetailResponse {
    Long id;
    String gradeName;
    Integer orderNo;

    public static GradeDetailResponse from(Grade grade) {
        return new GradeDetailResponse(grade.getId(), grade.getGradeName(), grade.getOrderNo());
    }
}