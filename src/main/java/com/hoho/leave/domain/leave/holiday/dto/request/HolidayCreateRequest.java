package com.hoho.leave.domain.leave.holiday.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayCreateRequest {
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @NotBlank
    String name;
}
