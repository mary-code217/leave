package com.hoho.leave.domain.leave.request.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class LeaveApprovalListResponse {
    Integer page;

    Integer size;

    List<LeaveApprovalResponse> leaveApprovals;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

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
