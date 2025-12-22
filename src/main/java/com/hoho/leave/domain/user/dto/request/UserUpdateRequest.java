package com.hoho.leave.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 사용자 정보 수정 요청 DTO.
 * <p>
 * 사용자 계정 정보 수정 시 필요한 정보를 담는다.
 * </p>
 */
@Data
public class UserUpdateRequest {
    @NotBlank(message = "이름은 필수 입력 입니다.")
    @Size(min = 2, max = 10, message = "이름은 2~10자 입니다.")
    String username;

    @NotBlank(message = "비밀번호는 필수 입력 입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 입니다.")
    String password;

    String teamName;

    String gradeName;

    String positionName;

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
