package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public static TeamListResponse of(Integer page, Integer size,
                                      List<TeamDetailResponse> teams, Page<Team> pages) {

        TeamListResponse response = new TeamListResponse();

        response.page = page;
        response.size = size;
        response.teams = teams;
        response.totalPage = pages.getTotalPages();
        response.totalElement = pages.getTotalElements();
        response.firstPage = pages.isFirst();
        response.lastPage = pages.isLast();

        return response;
    }
}
