package com.hoho.leave.domain.handover.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 발신 인수인계 목록 응답 DTO.
 * <p>
 * 사용자가 작성한 인수인계 목록과 페이징 정보를 반환한다.
 * </p>
 */
@Data
public class HandoverAuthorListResponse {

    /** 현재 페이지 번호 */
    Integer page;

    /** 페이지 크기 */
    Integer size;

    /** 인수인계 목록 */
    List<HandoverAuthorResponse> handoverNotes;

    /** 전체 페이지 수 */
    Integer totalPage;

    /** 전체 요소 수 */
    Long totalElement;

    /** 첫 페이지 여부 */
    Boolean firstPage;

    /** 마지막 페이지 여부 */
    Boolean lastPage;

    /**
     * Page 객체와 인수인계 목록으로 응답 DTO를 생성한다.
     *
     * @param page          페이징 정보
     * @param handoverNotes 인수인계 응답 목록
     * @return 발신 인수인계 목록 응답
     */
    public static HandoverAuthorListResponse of(Page<?> page, List<HandoverAuthorResponse> handoverNotes) {
        HandoverAuthorListResponse response = new HandoverAuthorListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.handoverNotes = handoverNotes;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
