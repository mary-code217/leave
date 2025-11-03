package com.hoho.leave.domain.leave.policy.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class LeaveTypeListResponse {
    Integer page;

    Integer size;

    List<LeaveTypeDetailResponse> leaveTypes;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

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
