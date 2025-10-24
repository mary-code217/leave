package com.hoho.leave.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {

    Integer page;
    Integer size;
    List<UserDetailResponse> users;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static UserListResponse of(Integer page, Integer size,
                                      List<UserDetailResponse> users, Integer totalPage, Long totalElement,
                                      Boolean firstPage, Boolean lastPage) {
        return new UserListResponse(page, size, users, totalPage, totalElement, firstPage, lastPage);
    }
}
