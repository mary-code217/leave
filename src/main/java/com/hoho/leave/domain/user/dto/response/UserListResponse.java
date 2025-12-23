package com.hoho.leave.domain.user.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 사용자 목록 응답 DTO.
 * 
 * 페이징된 사용자 목록과 페이지 정보를 클라이언트에게 전달한다.
 * 
 */
@Data
public class UserListResponse {
    Integer page;

    Integer size;

    List<UserDetailResponse> users;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;
    
    Boolean lastPage;

    /**
     * Page 객체와 사용자 목록으로부터 UserListResponse를 생성한다.
     *
     * @param page 페이지 정보
     * @param users 사용자 상세 정보 목록
     * @return 사용자 목록 응답 DTO
     */
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
