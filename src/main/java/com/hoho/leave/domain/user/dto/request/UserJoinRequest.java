package com.hoho.leave.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 사용자 회원가입 요청 DTO.
 * 
 * 새로운 사용자 계정 생성 시 필요한 정보를 담는다.
 * 
 */
@Data
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

    /**
     * 부서명을 설정한다. 빈 문자열인 경우 null로 변환한다.
     *
     * @param teamName 부서명
     */
    public void setTeamName(String teamName) {
        this.teamName = blankToNull(teamName);
    }

    /**
     * 직급명을 설정한다. 빈 문자열인 경우 null로 변환한다.
     *
     * @param gradeName 직급명
     */
    public void setGradeName(String gradeName) {
        this.gradeName = blankToNull(gradeName);
    }

    /**
     * 직책명을 설정한다. 빈 문자열인 경우 null로 변환한다.
     *
     * @param positionName 직책명
     */
    public void setPositionName(String positionName) {
        this.positionName = blankToNull(positionName);
    }

    /**
     * 빈 문자열을 null로 변환한다.
     *
     * @param s 변환할 문자열
     * @return null이거나 공백이면 null, 그 외에는 trim된 문자열
     */
    private static String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
