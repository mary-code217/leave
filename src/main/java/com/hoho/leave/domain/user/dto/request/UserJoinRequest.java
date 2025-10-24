package com.hoho.leave.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    @NotBlank(message = "이름은 필수 입력 입니다.")
    @Size(min = 2, max = 10, message = "이름은 2~10자 입니다.")
    String username;

    @NotBlank(message = "비밀번호는 필수 입력 입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 입니다.")
    String password;

    @NotBlank(message = "이메일은 필수 입력 입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    String email;

    @NotBlank(message = "사번은 필수 입력 입니다.")
    String employeeNo;

    @NotNull(message = "입사일은 필수 입력 입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate hireDate;

    @NotBlank(message = "사용자 권한은 필수 입력 입니다.")
    String role;

    String teamName;

    String gradeName;

    String positionName;

    @NotNull(message = "초기 휴가 설정은 필수 입니다.")
    @Digits(integer = 3, fraction = 2)
    @DecimalMin(value = "0.00", message = "휴가 일수는 음수가 될 수 없습니다.")
    BigDecimal balanceDays;

    public void setTeamName(String teamName) {
        this.teamName = blankToNull(teamName);
    }

    public void setGradeName(String gradeName) {
        this.gradeName = blankToNull(gradeName);
    }

    public void setPositionName(String positionName) {
        this.positionName = blankToNull(positionName);
    }

    private static String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
