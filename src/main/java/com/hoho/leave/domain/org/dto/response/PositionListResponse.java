package com.hoho.leave.domain.org.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

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

    public static  PositionListResponse of(Page<?> page, List<PositionDetailResponse> positions) {
        PositionListResponse positionListResponse = new PositionListResponse();

        positionListResponse.page = page.getNumber()+1;
        positionListResponse.size = page.getSize();
        positionListResponse.positions = positions;
        positionListResponse.totalPage = page.getTotalPages();
        positionListResponse.totalElement = page.getTotalElements();
        positionListResponse.firstPage = page.isFirst();
        positionListResponse.lastPage = page.isLast();

        return positionListResponse;
    }
}
