package com.hoho.leave.domain.audit.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class AuditLogListResponse {
    Integer page;

    Integer size;

    List<AuditLogDetailResponse> auditLogs;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

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
