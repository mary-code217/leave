package com.hoho.leave.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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

        UserListResponse response = new UserListResponse();

        response.page = page;
        response.size = size;
        response.users = users;
        response.totalPage = totalPage;
        response.totalElement = totalElement;
        response.firstPage = firstPage;
        response.lastPage = lastPage;

        return response;
    }
}
