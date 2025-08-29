package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.dto.response.TeamDetailResponse;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.repository.TeamRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createTeam(TeamCreateRequest teamCreateRequest) {
        if (teamRepository.existsByTeamName(teamCreateRequest.getTeamName())) {
            throw new BusinessException("생성 실패 - 이미 존재하는 부서명 입니다.");
        }

        Team team;
        if (teamCreateRequest.getParentId() == null) {
            team = Team.createTeam(teamCreateRequest.getTeamName(), teamCreateRequest.getOrderNo());
        } else {
            Team parent = teamRepository.findById(teamCreateRequest.getParentId())
                    .orElseThrow(() -> new BusinessException("생성 실패 - 존재 하지않는 상위 부서명 입니다."));
            team = parent.createChild(teamCreateRequest.getTeamName(), teamCreateRequest.getOrderNo());
        }

        teamRepository.save(team);
    }

    @Transactional
    public void updateTeam(Long teamId, TeamUpdateRequest teamUpdateRequest) {
        Team findTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("수정 실패 - 존재하지 않는 부서 입니다."));

        if (teamRepository.existsByTeamNameAndIdNot(teamUpdateRequest.getTeamName(), teamId)) {
            throw new BusinessException("수정 실패 - 이미 존재하는 부서명 입니다.");
        }

        Team newParent = null;
        if (teamUpdateRequest.getParentId() != null) {
            if (teamUpdateRequest.getParentId().equals(teamId)) {
                throw new BusinessException("수정 실패 - 자기 자신을 상위 부서로 지정할 수 없습니다.");
            }
            newParent = teamRepository.findById(teamUpdateRequest.getParentId())
                    .orElseThrow(() -> new BusinessException("수정 실패 - 존재하지 않는 상위 부서 입니다."));
        }

        findTeam.rename(teamUpdateRequest.getTeamName());
        findTeam.changeOrder(teamUpdateRequest.getOrderNo());
        findTeam.moveTo(newParent);
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("삭제 실패 - 존재하지 않는 부서 입니다."));

        if (teamRepository.existsByParentId(teamId)) {
            throw new BusinessException("삭제 실패 - 하위 부서가 존재합니다.");
        } else if (userRepository.existsByTeamId(teamId)) {
            throw new BusinessException("삭제 실패 - 부서에 소속된 유저가 존재합니다.");
        }

        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public TeamDetailResponse getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 부서 입니다."));

        Long userCount = userRepository.countByTeamId(teamId);
        Long childrenCount = teamRepository.countByParentId(teamId);

        return TeamDetailResponse.from(
                team.getId(),
                team.getTeamName(),
                team.getOrderNo(),
                (team.getParent()) == null ? 0L : team.getParent().getId(),
                (team.getParent()) == null ? "" : team.getParent().getTeamName(),
                userCount,
                childrenCount
        );
    }



}
