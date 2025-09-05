package com.hoho.leave.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    @NotBlank
    @Size(min = 8, max = 15)
    String username;

    @NotBlank
    @Size(min = 8, max = 15)
    String password;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String employeeNo;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate hireDate;

    @NotBlank
    String role;

    Long teamId;

    Long gradeId;

    Long positionId;
}
