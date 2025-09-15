package com.hoho.leave.domain.leave.request.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveApprovalListResponse {
    Integer page;
    Integer size;
    List<LeaveApprovalResponse> leaveApprovals;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static LeaveApprovalListResponse of(Integer page, Integer size,
                                               List<LeaveApprovalResponse> leaveApprovals,
                                               Integer totalPage, Long totalElement,
                                               Boolean firstPage, Boolean lastPage) {
        return LeaveApprovalListResponse.builder()
                .page(page)
                .size(size)
                .leaveApprovals(leaveApprovals)
                .totalPage(totalPage)
                .totalElement(totalElement)
                .firstPage(firstPage)
                .lastPage(lastPage).build();
    }
}
