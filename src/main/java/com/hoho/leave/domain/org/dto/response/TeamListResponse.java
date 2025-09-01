package com.hoho.leave.domain.org.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamListResponse {
    Integer page;
    Integer size;
    List<TeamDetailResponse> teams;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static TeamListResponse from(Integer page, Integer size,
                                             List<TeamDetailResponse> teams, Integer totalPage, Long totalElement,
                                             Boolean firstPage, Boolean lastPage) {
        return new TeamListResponse(page, size, teams, totalPage, totalElement, firstPage, lastPage);
    }
}
