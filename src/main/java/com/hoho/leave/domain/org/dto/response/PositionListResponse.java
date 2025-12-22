package com.hoho.leave.domain.org.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 직책 목록 응답 DTO.
 * <p>
 * 직책 목록과 페이징 정보를 반환하는 응답 데이터를 담는다.
 * </p>
 */
@Data
public class PositionListResponse {
    Integer page;

    Integer size;

    List<PositionDetailResponse> positions;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    /**
     * Page 객체와 직책 목록으로부터 응답 DTO를 생성한다.
     *
     * @param page 페이지 정보
     * @param positions 직책 상세 응답 목록
     * @return 직책 목록 응답 DTO
     */
    public static  PositionListResponse of(Page<?> page, List<PositionDetailResponse> positions) {
        PositionListResponse positionListResponse = new PositionListResponse();

        positionListResponse.page = page.getNumber()+1;
        positionListResponse.size = page.getSize();
        positionListResponse.positions = positions;
        positionListResponse.totalPage = page.getTotalPages();
        positionListResponse.totalElement = page.getTotalElements();
        positionListResponse.firstPage = page.isFirst();
        positionListResponse.lastPage = page.isLast();

        return positionListResponse;
    }
}
