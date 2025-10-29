package com.hoho.leave.domain.handover.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class HandoverAuthorListResponse {

    Integer page;

    Integer size;

    List<HandoverAuthorResponse> handoverNotes;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    public static HandoverAuthorListResponse of(Integer page, Integer size,
                                                List<HandoverAuthorResponse> handoverNotes,
                                                Integer totalPage, Long totalElement,
                                                Boolean firstPage, Boolean lastPage) {

        HandoverAuthorListResponse response = new HandoverAuthorListResponse();

        response.page = page;
        response.size = size;
        response.handoverNotes = handoverNotes;
        response.totalPage = totalPage;
        response.totalElement = totalElement;
        response.firstPage = firstPage;
        response.lastPage = lastPage;

        return response;
    }
}
