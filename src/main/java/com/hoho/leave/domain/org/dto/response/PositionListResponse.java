package com.hoho.leave.domain.org.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PositionListResponse {
    Integer page;

    Integer size;

    List<PositionDetailResponse> positions;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    public static  PositionListResponse of(Integer page, Integer size,
                                           List<PositionDetailResponse> positions, Integer totalPage, Long totalElement,
                                           Boolean firstPage, Boolean lastPage) {

        PositionListResponse positionListResponse = new PositionListResponse();

        positionListResponse.page = page;
        positionListResponse.size = size;
        positionListResponse.positions = positions;
        positionListResponse.totalPage = totalPage;
        positionListResponse.totalElement = totalElement;
        positionListResponse.firstPage = firstPage;
        positionListResponse.lastPage = lastPage;

        return positionListResponse;
    }
}
