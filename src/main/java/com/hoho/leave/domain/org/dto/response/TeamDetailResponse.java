package com.hoho.leave.domain.org.dto.response;

import com.hoho.leave.domain.org.entity.Team;
import lombok.Data;

@Data
public class TeamDetailResponse {

    Long teamId;

    String teamName;

    Integer orderNo;

    Long parentId;

    String parentName;

    Long userCount;

    Long childrenCount;

    public static TeamDetailResponse of(Team team, Long userCount,
                                        Long childrenCount) {

        TeamDetailResponse response = new TeamDetailResponse();

        response.teamId = team.getId();
        response.teamName = team.getTeamName();
        response.orderNo = team.getOrderNo();
        response.parentId = team.getParent() == null ? 0L : team.getParent().getId();
        response.parentName = team.getParent() == null ? "" : team.getParent().getTeamName();
        response.userCount = userCount;
        response.childrenCount = childrenCount;

        return response;
    }
}
