package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDetailResponse {
    Long id;
    String positionName;
    Integer orderNo;

    public static PositionDetailResponse from(Position position) {
        return new PositionDetailResponse(position.getId(), position.getPositionName(), position.getOrderNo());
    }
}
