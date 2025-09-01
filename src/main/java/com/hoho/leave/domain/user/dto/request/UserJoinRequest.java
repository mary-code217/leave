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

    @NotBlank @NotNull
    @Size(min = 8, max = 15)
    String username;

    @NotBlank @NotNull
    @Size(min = 8, max = 15)
    String password;

    @NotBlank @NotNull
    @Email
    String email;

    @NotNull @NotBlank
    String employeeNo;

    @NotNull @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate hireDate;

    @NotNull @NotBlank
    String role;

    @NotNull @NotBlank
    Long teamId;

    @NotNull @NotBlank
    Long gradeId;

    @NotNull @NotBlank
    Long positionId;
}
