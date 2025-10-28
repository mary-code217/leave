package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Position;
import lombok.Data;

@Data
public class PositionDetailResponse {
    Long id;

    String positionName;

    Integer orderNo;

    public static PositionDetailResponse of(Position position) {
        PositionDetailResponse response = new PositionDetailResponse();

        response.id = position.getId();
        response.positionName = position.getPositionName();
        response.orderNo = position.getOrderNo();

        return response;
    }
}
