package com.hoho.leave.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserJoinRequest {
    String username;
    String password;
    String email;
    String employeeNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate hireDate;

    String role;
    String team;
    String grade;
    String position;
}
