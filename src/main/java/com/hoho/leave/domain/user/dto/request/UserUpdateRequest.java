package com.hoho.leave.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    String username;
    String password;
    String teamName;
    String gradeName;
    String positionName;
}
