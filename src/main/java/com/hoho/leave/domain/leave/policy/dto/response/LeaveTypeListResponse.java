package com.hoho.leave.domain.leave.policy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeListResponse {
    Integer page;
    Integer size;
    List<LeaveTypeDetailResponse> leaveTypes;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static LeaveTypeListResponse from(Integer page, Integer size,
                                             List<LeaveTypeDetailResponse> leaveTypes,
                                             Integer totalPage, Long totalElement,
                                             Boolean firstPage, Boolean lastPage) {

        return new LeaveTypeListResponse(page, size, leaveTypes, totalPage, totalElement, firstPage, lastPage);
    }
}
