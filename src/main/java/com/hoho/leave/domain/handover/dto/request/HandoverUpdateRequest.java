package com.hoho.leave.domain.handover.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 인수인계 수정 요청 DTO.
 */
@Data
public class HandoverUpdateRequest {

    /** 작성자 ID */
    @NotNull
    Long authorId;

    /** 수신자 ID 목록 */
    @NotEmpty
    List<@NotNull Long> recipientIds;

    /** 제목 */
    @NotBlank
    String title;

    /** 내용 */
    @NotBlank
    String content;
}
