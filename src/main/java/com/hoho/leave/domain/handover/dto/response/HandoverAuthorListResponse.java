package com.hoho.leave.domain.handover.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

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

    public static HandoverAuthorListResponse of(Page<?> page, List<HandoverAuthorResponse> handoverNotes) {
        HandoverAuthorListResponse response = new HandoverAuthorListResponse();

        response.page = page.getNumber();
        response.size = page.getSize();
        response.handoverNotes = handoverNotes;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
