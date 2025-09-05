package com.hoho.leave.domain.leave.handover.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class HandoverCreateRequest {
    @NotNull
    Long authorId;

    @NotEmpty
    List<@NotNull Long> recipientIds;
    @NotBlank
    String title;
    @NotBlank
    String content;
}
