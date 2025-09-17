package com.hoho.leave.domain.handover.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandoverAuthorListResponse {
    Integer page;
    Integer size;
    List<HandoverAuthorResponse> handoverNotes;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static HandoverAuthorListResponse from(Integer page, Integer size,
                                                  List<HandoverAuthorResponse> handoverNotes,
                                                  Integer totalPage, Long totalElement,
                                                  Boolean firstPage, Boolean lastPage) {
        return new HandoverAuthorListResponse(page, size, handoverNotes, totalPage, totalElement, firstPage, lastPage);
    }
}
