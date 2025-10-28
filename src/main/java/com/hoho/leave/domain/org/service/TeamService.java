package com.hoho.leave.domain.org.service;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.dto.response.TeamDetailResponse;
import com.hoho.leave.domain.org.dto.response.TeamListResponse;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.repository.TeamRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /**
     * 부서 생성
     */
    @Transactional
    public void createTeam(TeamCreateRequest request) {
        checkDuplicateTeam(request.getTeamName());

        Team team;
        if (request.getParentId() == null) {
            team = Team.createTeam(request);
        } else {
            Team parent = getTeamEntity(request.getParentId());
            team = parent.createChild(request);
        }

        teamRepository.save(team);
    }

    /**
     * 부서 수정
     */
    @Transactional
    public void updateTeam(Long teamId, TeamUpdateRequest request) {
        Team team = getTeamEntity(teamId);
        checkDuplicateTeam(request.getTeamName(), teamId);
        updateParent(team, request.getParentId());
        team.changeOrder(request.getOrderNo());
        team.rename(request.getTeamName());
    }

    /**
     * 부서 삭제
     */
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = getTeamEntity(teamId);
        checkExistChildOrUser(teamId);
        teamRepository.delete(team);
    }
    
    /**
     * 부서 단일 조회(화면)
     */
    @Transactional(readOnly = true)
    public TeamDetailResponse getTeam(Long teamId) {
        Team team = getTeamEntity(teamId);
        Long userCount = userRepository.countByTeamId(teamId);
        Long childrenCount = teamRepository.countByParentId(teamId);
        return TeamDetailResponse.of(team, userCount, childrenCount);
    }

    /**
     * 부서 전체 조회(화면)
     */
    @Transactional(readOnly = true)
    public TeamListResponse getAllTeams(Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("orderNo")));

        Page<Team> pages = teamRepository.findAll(pageable);
        List<TeamDetailResponse> list = new ArrayList<>();

        for (Team team : pages.toList()) {
            Long userCount = userRepository.countByTeamId(team.getId());
            Long childrenCount = teamRepository.countByParentId(team.getId());

            list.add(TeamDetailResponse.of(team, userCount, childrenCount));
        }

        return TeamListResponse.of(page, size, list, pages);
    }

    /**
     * 부서 엔티티 조회
     */
    public Team getTeamEntity(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Not Found Team : " + teamId));
    }

    /**
     * 중복 부서 체크
     * @param teamName 부서명
     */
    private void checkDuplicateTeam(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new DuplicateException("Duplicate TeamName : " + teamName);
        }
    }

    /**
     * 중복 부서 체크
     * @param teamName 부서명
     * @param teamId 팀ID
     */
    private void checkDuplicateTeam(String teamName, Long teamId) {
        if (teamRepository.existsByTeamNameAndIdNot(teamName, teamId)) {
            throw new DuplicateException("Duplicate TeamName : " + teamName);
        }
    }

    /**
     * 상위 부서 수정
     */
    private void updateParent(Team team, Long parentId) {
        if (parentId != null) {
            if (parentId.equals(team.getId())) {
                throw new BusinessException("Update Failed : 자기 자신을 상위 부서로 지정할 수 없습니다.");
            }
            Team parent = teamRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Not Found Team : " + parentId));
            team.moveTo(parent);
        }
    }

    /**
     * 하위 부서 또는 소속 사용자 존재 여부 체크
     */
    private void checkExistChildOrUser(Long teamId) {
        if (teamRepository.existsByParentId(teamId)) {
            throw new BusinessException("Delete Failed : 하위 부서가 존재합니다.");
        } else if (userRepository.existsByTeamId(teamId)) {
            throw new BusinessException("Delete Failed : 소속 사용자가 존재합니다.");
        }
    }
}
