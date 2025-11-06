package com.hoho.leave.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailResponse {
    Long id;

    String username;

    String email;

    String employeeNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate hireDate;

    String role;

    String teamName;

    String gradeName;

    String positionName;

    String active;
    
    public static UserDetailResponse of(User user) {
        UserDetailResponse response = new UserDetailResponse();

        response.id = user.getId();
        response.username = user.getUsername();
        response.email = user.getEmail();
        response.employeeNo = user.getEmployeeNo();
        response.hireDate = user.getHireDate();
        response.role = user.getRole().toString();
        response.teamName = user.getTeam() != null ? user.getTeam().getTeamName() : "";
        response.gradeName = user.getGrade() != null ? user.getGrade().getGradeName() : "";
        response.positionName = user.getPosition() != null ? user.getPosition().getPositionName() : "";
        response.active = user.isActive() ? "재직" : "휴직or퇴직";

        return response;
    }
}
