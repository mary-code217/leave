package com.hoho.leave.domain.org.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 팀 목록 응답 DTO.
 * 
 * 팀 목록과 페이징 정보를 반환하는 응답 데이터를 담는다.
 * 
 */
@Data
public class TeamListResponse {
    Integer page;

    Integer size;

    List<TeamDetailResponse> teams;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    /**
     * Page 객체와 팀 목록으로부터 응답 DTO를 생성한다.
     *
     * @param page 페이지 정보
     * @param teams 팀 상세 응답 목록
     * @return 팀 목록 응답 DTO
     */
    public static TeamListResponse of(Page<?> page, List<TeamDetailResponse> teams) {
        TeamListResponse response = new TeamListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.teams = teams;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}
