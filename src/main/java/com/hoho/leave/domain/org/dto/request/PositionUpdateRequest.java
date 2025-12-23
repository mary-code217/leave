package com.hoho.leave.domain.org.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 직책 수정 요청 DTO.
 * 
 * 기존 직책 정보를 수정할 때 사용되는 요청 데이터를 담는다.
 * 
 */
@Data
public class PositionUpdateRequest {
    @NotBlank(message = "직책은 필수입니다.")
    String positionName;

    @PositiveOrZero(message = "정렬순서는 0 이상의 정수여야 합니다.")
    Integer orderNo;
}
