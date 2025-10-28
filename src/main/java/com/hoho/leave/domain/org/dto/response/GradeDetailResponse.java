package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Grade;
import lombok.Data;

@Data
public class GradeDetailResponse {
    Long id;

    String gradeName;

    Integer orderNo;

    public static GradeDetailResponse of(Grade grade) {
        GradeDetailResponse response = new GradeDetailResponse();

        response.id = grade.getId();
        response.gradeName = grade.getGradeName();
        response.orderNo = grade.getOrderNo();

        return response;
    }
}