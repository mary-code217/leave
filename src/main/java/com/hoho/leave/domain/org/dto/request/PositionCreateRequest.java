package com.hoho.leave.domain.org.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 직책 생성 요청 DTO.
 * 
 * 새로운 직책을 등록할 때 사용되는 요청 데이터를 담는다.
 * 
 */
@Data
public class PositionCreateRequest {
    @NotBlank(message = "직책은 필수입니다.")
    String positionName;

    @PositiveOrZero(message = "정렬순서는 0 이상의 정수여야 합니다.")
    Integer orderNo;
}
