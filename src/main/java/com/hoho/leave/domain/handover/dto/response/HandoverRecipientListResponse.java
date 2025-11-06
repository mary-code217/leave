package com.hoho.leave.domain.handover.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class HandoverRecipientListResponse {
    Integer page;

    Integer size;

    List<HandoverRecipientResponse> recipients;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    public static HandoverRecipientListResponse of(Page<?> page, List<HandoverRecipientResponse> recipients) {
        HandoverRecipientListResponse response = new HandoverRecipientListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.recipients = recipients;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
