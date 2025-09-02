package com.hoho.leave.domain.leave.request.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestListResponse {
    Integer page;
    Integer size;
    List<LeaveRequestDetailResponse> leaveTypes;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static LeaveRequestListResponse from(Integer page, Integer size,
                                           List<LeaveRequestDetailResponse> leaveTypes,
                                           Integer totalPage, Long totalElement,
                                           Boolean firstPage, Boolean lastPage) {

        return new LeaveRequestListResponse(page, size, leaveTypes, totalPage, totalElement, firstPage, lastPage);
    }
}
