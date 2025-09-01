package com.hoho.leave.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.entity.Team;
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
    
    public static UserDetailResponse from(Long id, String username,
                                          String email, String employeeNo,
                                          LocalDate hireDate, String role, 
                                          String teamName, String gradeName, String positionName, boolean active) {
        
        return new UserDetailResponse(
                id,
                username,
                email,
                employeeNo,
                hireDate,
                role,
                teamName,
                gradeName,
                positionName,
                active ? "재직" : "휴직"
        );
    }
}
