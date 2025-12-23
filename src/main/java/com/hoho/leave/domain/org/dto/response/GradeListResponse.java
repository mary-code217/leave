package com.hoho.leave.domain.org.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 직급 목록 응답 DTO.
 * 
 * 직급 목록과 페이징 정보를 반환하는 응답 데이터를 담는다.
 * 
 */
@Data
public class GradeListResponse {
    Integer page;

    Integer size;

    List<GradeDetailResponse> grades;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    /**
     * Page 객체와 직급 목록으로부터 응답 DTO를 생성한다.
     *
     * @param page 페이지 정보
     * @param grades 직급 상세 응답 목록
     * @return 직급 목록 응답 DTO
     */
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
