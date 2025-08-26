package com.hoho.leave.domain.org.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GradeCreateRequest {

    @NotBlank
    String grade;

    Integer orderNo;
}
