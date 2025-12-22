package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Grade;
import lombok.Data;

/**
 * 직급 상세 응답 DTO.
 * <p>
 * 직급의 상세 정보를 반환하는 응답 데이터를 담는다.
 * </p>
 */
@Data
public class GradeDetailResponse {
    Long id;

    String gradeName;

    Integer orderNo;

    /**
     * Grade 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param grade 직급 엔티티
     * @return 직급 상세 응답 DTO
     */
    public static GradeDetailResponse of(Grade grade) {
        GradeDetailResponse response = new GradeDetailResponse();

        response.id = grade.getId();
        response.gradeName = grade.getGradeName();
        response.orderNo = grade.getOrderNo();

        return response;
    }
}