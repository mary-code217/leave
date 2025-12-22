package com.hoho.leave.domain.leave.policy.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 휴가 유형 목록 응답 DTO.
 * <p>
 * 휴가 유형 목록과 페이징 정보를 반환하는 응답 데이터를 담는다.
 * </p>
 */
@Data
public class LeaveTypeListResponse {
    Integer page;

    Integer size;

    List<LeaveTypeDetailResponse> leaveTypes;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    /**
     * Page 객체와 휴가 유형 목록으로부터 응답 DTO를 생성한다.
     *
     * @param page 페이지 정보
     * @param leaveTypes 휴가 유형 상세 응답 목록
     * @return 휴가 유형 목록 응답 DTO
     */
    public static LeaveTypeListResponse of(Page<?> page, List<LeaveTypeDetailResponse> leaveTypes) {
        LeaveTypeListResponse response = new LeaveTypeListResponse();

        response.page = page.getNumber() + 1;
        response.size = page.getSize();
        response.leaveTypes = leaveTypes;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
