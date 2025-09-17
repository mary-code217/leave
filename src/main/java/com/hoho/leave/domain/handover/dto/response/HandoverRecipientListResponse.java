package com.hoho.leave.domain.handover.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandoverRecipientListResponse {
    Integer page;
    Integer size;
    List<HandoverRecipientResponse> recipients;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static HandoverRecipientListResponse from(Integer page, Integer size,
                                                     List<HandoverRecipientResponse> recipients,
                                                     Integer totalPage, Long totalElement,
                                                     Boolean firstPage, Boolean lastPage) {

        return new HandoverRecipientListResponse(page, size, recipients, totalPage, totalElement, firstPage, lastPage);
    }
}
