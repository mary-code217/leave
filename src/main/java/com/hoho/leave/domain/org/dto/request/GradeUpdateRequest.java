package com.hoho.leave.domain.org.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class GradeUpdateRequest {
    @NotBlank(message = "직급명은 필수입니다.")
    String gradeName;

    @PositiveOrZero(message = "정렬순서는 0 이상의 정수여야 합니다.")
    Integer orderNo;
}
