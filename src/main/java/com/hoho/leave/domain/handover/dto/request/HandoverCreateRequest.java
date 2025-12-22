package com.hoho.leave.domain.handover.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 인수인계 생성 요청 DTO.
 */
@Data
public class HandoverCreateRequest {

    /** 작성자 ID */
    @NotNull
    Long authorId;

    /** 수신자 ID 목록 */
    @NotEmpty(message = "수신자를 1명 이상 설정해주세요.")
    List<@NotNull Long> recipientIds;

    /** 제목 */
    @NotBlank(message = "제목은 필수 입력 입니다.")
    String title;

    /** 내용 */
    @NotBlank(message = "본문은 필수 입력 입니다.")
    String content;
}
