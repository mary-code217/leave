package com.hoho.leave.domain.handover.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandoverUpdateRequest {
    @NotNull
    Long authorId;

    @NotEmpty
    List<@NotNull Long> recipientIds;
    @NotBlank
    String title;
    @NotBlank
    String content;
}
