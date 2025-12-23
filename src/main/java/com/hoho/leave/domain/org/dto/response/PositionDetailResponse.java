package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Position;
import lombok.Data;

/**
 * 직책 상세 응답 DTO.
 * 
 * 직책의 상세 정보를 반환하는 응답 데이터를 담는다.
 * 
 */
@Data
public class PositionDetailResponse {
    Long id;

    String positionName;

    Integer orderNo;

    /**
     * Position 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param position 직책 엔티티
     * @return 직책 상세 응답 DTO
     */
    public static PositionDetailResponse of(Position position) {
        PositionDetailResponse response = new PositionDetailResponse();

        response.id = position.getId();
        response.positionName = position.getPositionName();
        response.orderNo = position.getOrderNo();

        return response;
    }
}
