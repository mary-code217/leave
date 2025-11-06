package com.hoho.leave.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
