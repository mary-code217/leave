package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.repository.GradeRepository;
import com.hoho.leave.domain.org.repository.TeamRepository;
import com.hoho.leave.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void createTeam(TeamCreateRequest teamCreateRequest) {
        // 1) 부모 조회 (루트면 null)
        Team parent = null;
        if (teamCreateRequest.getParentId() != null) {
            parent = teamRepository.findById(teamCreateRequest.getParentId())
                    .orElseThrow(() -> new BusinessException("상위 부서를 찾을 수 없습니다."));
        }

        // 2) (parent_id, team_name) 조합 중복 체크
        boolean dup = (parent == null)
                ? teamRepository.existsByParentIsNullAndTeamName(teamCreateRequest.getTeamName())
                : teamRepository.existsByParentIdAndTeamName(parent.getId(), teamCreateRequest.getTeamName());
        if (dup) {
            throw new BusinessException("같은 상위 부서 안에 동일한 부서가 이미 존재합니다.");
        }

        Team team = (parent == null)
                ? Team.createRoot(teamCreateRequest.getTeamName(), teamCreateRequest.getOrderNo())
                : parent.createChild(teamCreateRequest.getTeamName(), teamCreateRequest.getOrderNo());

        // 저장은 자식만 save 해도 OK (ManyToOne이 주인이라 child만 저장하면 됨)
        teamRepository.save(team);
    }

    @Transactional
    public void updateTeam(Long teamId, TeamUpdateRequest teamUpdateRequest) {
        // 1) 대상 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("부서를 찾을 수 없습니다."));

        // 2) 새 부모 로드 (null이면 루트)
        Team newParent = null;
        if (teamUpdateRequest.getParentId() != null) {
            newParent = teamRepository.findById(teamUpdateRequest.getParentId())
                    .orElseThrow(() -> new BusinessException("상위 부서를 찾을 수 없습니다."));
        }

        // 3) 자기 자신/사이클 방지
        if (newParent != null && newParent.getId().equals(team.getId())) {
            throw new BusinessException("자기 자신을 상위 부서로 지정할 수 없습니다.");
        }
        for (Team p = newParent; p != null; p = p.getParent()) {
            if (p.getId().equals(team.getId())) {
                throw new BusinessException("하위 부서로 이동할 수 없습니다.");
            }
        }

        // 4) (parent, name) 중복 체크 (자기 자신 제외)
        boolean dup = (newParent == null)
                ? teamRepository.existsByParentIsNullAndTeamNameAndIdNot(teamUpdateRequest.getTeamName(), teamId)
                : teamRepository.existsByParent_IdAndTeamNameAndIdNot(newParent.getId(), teamUpdateRequest.getTeamName(), teamId);
        if (dup) {
            throw new BusinessException("같은 상위 부서 안에 동일한 팀명이 이미 존재합니다.");
        }

        // 5) 변경 적용 (JPA 변경감지)
        team.rename(teamUpdateRequest.getTeamName());
        team.changeOrder(teamUpdateRequest.getOrderNo());
        team.moveTo(newParent);
    }
}
