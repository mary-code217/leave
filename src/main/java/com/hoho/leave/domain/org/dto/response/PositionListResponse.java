package com.hoho.leave.domain.org.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionListResponse {
    Integer page;
    Integer size;
    List<PositionDetailResponse> positions;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static  PositionListResponse from(Integer page, Integer size,
                                          List<PositionDetailResponse> positions, Integer totalPage, Long totalElement,
                                          Boolean firstPage, Boolean lastPage) {
        return new PositionListResponse(page, size, positions, totalPage, totalElement, firstPage, lastPage);
    }
}
