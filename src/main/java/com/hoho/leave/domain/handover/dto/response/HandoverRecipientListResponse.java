package com.hoho.leave.domain.handover.dto.response;

import lombok.Data;

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

    public static HandoverRecipientListResponse of(Integer page, Integer size,
                                                   List<HandoverRecipientResponse> recipients,
                                                   Integer totalPage, Long totalElement,
                                                   Boolean firstPage, Boolean lastPage) {

        HandoverRecipientListResponse response = new HandoverRecipientListResponse();

        response.page = page;
        response.size = size;
        response.recipients = recipients;
        response.totalPage = totalPage;
        response.totalElement = totalElement;
        response.firstPage = firstPage;
        response.lastPage = lastPage;

        return response;
    }
}
