package com.hoho.leave.domain.leave.account.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 휴가 원장 목록 응답 DTO.
 * <p>
 * 페이징된 휴가 원장 목록 정보를 담는다.
 * </p>
 */
@Data
public class LeaveLedgerListResponse {
    Integer page;

    Integer size;

    List<LeaveLedgerDetailResponse> leaveLedgers;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    /**
     * 페이지 정보와 휴가 원장 목록으로 응답 DTO를 생성한다.
     *
     * @param page 페이지 정보
     * @param leaveLedgers 휴가 원장 목록
     * @return 휴가 원장 목록 응답
     */
    public static LeaveLedgerListResponse of(Page<?> page, List<LeaveLedgerDetailResponse> leaveLedgers) {
        LeaveLedgerListResponse response = new LeaveLedgerListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.leaveLedgers = leaveLedgers;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
