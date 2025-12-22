package com.hoho.leave.domain.leave.request.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 휴가 결재 목록 응답 DTO.
 * <p>
 * 페이지네이션된 휴가 결재 목록 정보를 담는다.
 * </p>
 */
@Data
public class LeaveApprovalListResponse {
    /**
     * 현재 페이지 번호
     */
    Integer page;

    /**
     * 페이지 크기
     */
    Integer size;

    /**
     * 휴가 결재 목록
     */
    List<LeaveApprovalResponse> leaveApprovals;

    /**
     * 전체 페이지 수
     */
    Integer totalPage;

    /**
     * 전체 요소 개수
     */
    Long totalElement;

    /**
     * 첫 페이지 여부
     */
    Boolean firstPage;

    /**
     * 마지막 페이지 여부
     */
    Boolean lastPage;

    /**
     * 페이지 정보와 결재 목록으로 응답 DTO를 생성한다.
     *
     * @param page 페이지 정보
     * @param leaveApprovals 휴가 결재 목록
     * @return 휴가 결재 목록 응답 DTO
     */
    public static LeaveApprovalListResponse of(Page<?> page, List<LeaveApprovalResponse> leaveApprovals) {
        LeaveApprovalListResponse response = new LeaveApprovalListResponse();

        response.page = page.getNumber() + 1;
        response.size = page.getSize();
        response.leaveApprovals = leaveApprovals;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
