package com.hoho.leave.domain.org.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class TeamListResponse {
    Integer page;

    Integer size;

    List<TeamDetailResponse> teams;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

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
