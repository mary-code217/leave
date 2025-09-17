package com.hoho.leave.domain.handover.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class HandoverCreateRequest {
    @NotNull
    Long authorId;

    @NotEmpty(message = "수신자를 1명 이상 설정해주세요.")
    List<@NotNull Long> recipientIds;
    @NotBlank(message = "제목은 필수 입력 입니다.")
    String title;
    @NotBlank(message = "본문은 필수 입력 입니다.")
    String content;
}
