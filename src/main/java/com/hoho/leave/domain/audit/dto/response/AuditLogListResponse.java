package com.hoho.leave.domain.audit.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 감사 로그 목록 응답 DTO.
 * 
 * 페이징 정보와 함께 감사 로그 목록을 반환한다.
 * 
 */
@Data
public class AuditLogListResponse {

    /** 현재 페이지 번호 */
    Integer page;

    /** 페이지 크기 */
    Integer size;

    /** 감사 로그 목록 */
    List<AuditLogDetailResponse> auditLogs;

    /** 전체 페이지 수 */
    Integer totalPage;

    /** 전체 요소 수 */
    Long totalElement;

    /** 첫 페이지 여부 */
    Boolean firstPage;

    /** 마지막 페이지 여부 */
    Boolean lastPage;

    /**
     * Page 객체와 감사 로그 목록으로부터 응답 DTO를 생성한다.
     *
     * @param page      페이징 정보
     * @param auditLogs 감사 로그 상세 목록
     * @return 감사 로그 목록 응답
     */
    public static AuditLogListResponse of(Page<?> page, List<AuditLogDetailResponse> auditLogs) {
        AuditLogListResponse response = new AuditLogListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.auditLogs = auditLogs;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
