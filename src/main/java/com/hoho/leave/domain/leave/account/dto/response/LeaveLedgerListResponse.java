package com.hoho.leave.domain.leave.account.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class LeaveLedgerListResponse {
    Integer page;

    Integer size;

    List<LeaveLedgerDetailResponse> leaveLedgers;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

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
