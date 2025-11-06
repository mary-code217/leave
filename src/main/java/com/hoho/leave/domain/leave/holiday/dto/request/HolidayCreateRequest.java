package com.hoho.leave.domain.leave.holiday.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayCreateRequest {
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @NotBlank
    String name;
}
