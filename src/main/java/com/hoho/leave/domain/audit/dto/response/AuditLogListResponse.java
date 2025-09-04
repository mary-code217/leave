package com.hoho.leave.domain.audit.dto.response;

import com.hoho.leave.domain.org.dto.response.GradeDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogListResponse {
    Integer page;
    Integer size;
    List<AuditLogResponse> auditLogs;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static AuditLogListResponse from(Integer page, Integer size,
                                            List<AuditLogResponse> auditLogs,
                                            Integer totalPage, Long totalElement,
                                            Boolean firstPage, Boolean lastPage) {
        return new AuditLogListResponse(page, size, auditLogs, totalPage, totalElement, firstPage, lastPage);
    }
}
