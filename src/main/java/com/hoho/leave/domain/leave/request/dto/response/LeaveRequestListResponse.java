package com.hoho.leave.domain.leave.request.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class LeaveRequestListResponse {
    Integer page;

    Integer size;

    List<LeaveRequestDetailResponse> leaveTypes;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    public static LeaveRequestListResponse of(Page<?> page, List<LeaveRequestDetailResponse> leaveTypes) {
        LeaveRequestListResponse response = new LeaveRequestListResponse();

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
