package com.hoho.leave.domain.user.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

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

    public static UserListResponse of(Page<?> page, List<UserDetailResponse> users) {
        UserListResponse response = new UserListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.users = users;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
