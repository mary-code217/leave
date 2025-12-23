package com.hoho.leave.domain.org.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 팀 수정 요청 DTO.
 * 
 * 기존 팀 정보를 수정할 때 사용되는 요청 데이터를 담는다.
 * 
 */
@Data
public class TeamUpdateRequest {
    @NotBlank(message = "팀명은 필수입니다.")
    private String teamName;   // DB: team_name

    // 상위팀 ID (루트면 null)
    @Positive(message = "parentId는 양의 정수여야 합니다.")
    private Long parentId;

    // 선택: 형제 간 정렬용. 지정 안 하면 이름순 보조정렬.
    @PositiveOrZero(message = "정렬순서는 0 이상의 정수여야 합니다.")
    private Integer orderNo;
}
