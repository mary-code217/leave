package com.hoho.leave.domain.org.dto.request;

import lombok.Data;

import jakarta.validation.constraints.*;

/**
 * 팀 생성 요청 DTO.
 * <p>
 * 새로운 팀을 등록할 때 사용되는 요청 데이터를 담는다.
 * </p>
 */
@Data
public class TeamCreateRequest {
    @NotBlank(message = "팀명은 필수입니다.")
    private String teamName;   // DB: team_name

    @Positive(message = "parentId는 양의 정수여야 합니다.")
    private Long parentId;

    @PositiveOrZero(message = "정렬순서는 0 이상의 정수여야 합니다.")
    private Integer orderNo;
}
