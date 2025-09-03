package com.hoho.leave.domain.leave.request.dto.request;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestUpdateRequest {
    @NotNull @NotBlank
    LeaveRequestStatus status;
}
