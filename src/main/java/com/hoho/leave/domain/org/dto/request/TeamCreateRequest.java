package com.hoho.leave.domain.org.dto.request;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class TeamCreateRequest {
    @NotBlank(message = "팀명은 필수입니다.")
    private String teamName;   // DB: team_name

    @Positive(message = "parentId는 양의 정수여야 합니다.")
    private Long parentId;

    @PositiveOrZero(message = "정렬순서는 0 이상의 정수여야 합니다.")
    private Integer orderNo;
}
