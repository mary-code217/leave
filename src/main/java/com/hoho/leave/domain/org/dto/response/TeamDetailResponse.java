package com.hoho.leave.domain.org.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDetailResponse {

    Long teamId;
    String teamName;
    Integer orderNo;
    Long parentId;
    String parentName;

    Long userCount;
    Long childrenCount;

    public static TeamDetailResponse from(Long teamId, String teamName,
                                          Integer orderNo, Long parentId,
                                          String parentName, Long userCount,
                                          Long childrenCount) {

        return new TeamDetailResponse(teamId, teamName, orderNo, parentId, parentName, userCount, childrenCount);
    }
}
